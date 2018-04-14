package io.github.aedans.proton.ui.components;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import fj.data.List;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.logic.Plugins;
import io.github.aedans.proton.ui.*;
import org.pf4j.ExtensionPoint;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public final class SearchBox implements TextComponent {
    private static final List<KeyListener> keyListeners = Plugins.all(KeyListener.class)
            .append(TextBox.keyListeners
                    .map(x -> (KeyListener) (searchBox, keyStroke) ->
                            searchBox.mapTextBox(y -> x.apply(searchBox.textBox, keyStroke))));

    public final TextBox textBox;
    public final int max;
    public final Stream<Seq<TextCharacter>> search;
    public final BiFunction<String, String, Boolean> filter;

    public SearchBox() {
        this(new TextBox(), Terminal.size().getRows() - 1, Stream.nil(), filterContains);
    }

    public SearchBox(
            TextBox textBox,
            int max,
            Stream<Seq<TextCharacter>> search,
            BiFunction<String, String, Boolean> filter
    ) {
        this.textBox = textBox;
        this.max = max;
        this.search = search;
        this.filter = filter;
    }

    public SearchBox mapTextBox(UnaryOperator<TextBox> fn) {
        return new SearchBox(fn.apply(textBox), max, search, filter);
    }

    public SearchBox mapMax(UnaryOperator<Integer> fn) {
        return new SearchBox(textBox, fn.apply(max), search, filter);
    }

    public SearchBox mapSearch(UnaryOperator<Stream<Seq<TextCharacter>>> fn) {
        return new SearchBox(textBox, max, fn.apply(search), filter);
    }

    public SearchBox withTextBox(TextBox textBox) {
        return mapTextBox(x -> textBox);
    }

    public SearchBox withMax(int i) {
        return mapMax(x -> i);
    }

    public SearchBox withSearch(Stream<Seq<TextCharacter>> search) {
        return mapSearch(x -> search);
    }

    @Override
    public SearchBox accept(KeyStroke keyStroke) {
        return keyListeners
                .foldLeft((searchBox, keyListener) -> keyListener.apply(searchBox, keyStroke), this)
                .mapTextBox(x -> x.fix(Terminal.size()));
    }

    @Override
    public void render(TerminalPosition offset, TerminalSize size) {
        Terminal.clear(offset, size.withRows(1));
        textBox.render(offset, size.withRows(1));

        Stream<Seq<TextCharacter>> matches = search.filter(x -> filter.apply(TextString.toString(x),
                TextString.toString(textBox.text.toStream())));

        TerminalPosition searchOffset = offset.withRelativeRow(1);
        Terminal.clear(searchOffset, size.withRows(matches.length()));

        if (matches.length() > max) {
            Seq<TextCharacter> message = TextString.fromString("... and " + (matches.length() - max) + " more");
            TextString.render(matches.take(max - 1).snoc(message), searchOffset);
        } else {
            TextString.render(matches.take(max), searchOffset);
        }
    }

    @Override
    public String text() {
        return textBox.text();
    }

    public static final BiFunction<String, String, Boolean> filterContains = String::contains;

    public interface KeyListener extends ExtensionPoint {
        SearchBox apply(SearchBox searchBox, KeyStroke keyStroke);
    }
}
