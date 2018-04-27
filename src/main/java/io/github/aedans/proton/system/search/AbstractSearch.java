package io.github.aedans.proton.system.search;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.system.text.Text;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.AbstractImmutable;
import io.github.aedans.proton.util.Key;
import org.immutables.value.Value;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractSearch implements Ast {
    public static final Key key = Key.of(AbstractSearch.class);

    public static final BiFunction<String, String, Boolean> filterContains = String::contains;

    @Value.Default
    public Text text() {
        return Text.builder().build();
    }

    @Value.Default
    public int cursor() {
        return 0;
    }

    @Value.Default
    public int scroll() {
        return 0;
    }

    @Value.Default
    public Stream<Seq<TextCharacter>> searchSpace() {
        return Stream.nil();
    }

    @Value.Default
    public BiFunction<String, String, Boolean> filter() {
        return filterContains;
    }

    @Value.Default
    public TerminalSize size() {
        return Terminal.size().runUnsafe();
    }

    @Value.Lazy
    public Stream<Seq<TextCharacter>> filteredSearchSpace() {
        return searchSpace().filter(x -> filter().apply(TextString.toString(x), TextString.toString(text().text())));
    }

    public int row() {
        return cursor() + scroll();
    }

    @Value.Check
    public AbstractSearch normalize() {
        int length = filteredSearchSpace().take(row() + size().getRows()).length();

        if (cursor() < 0) {
            return this
                    .mapCursor(x -> 0)
                    .withScroll(row());
        } else if (cursor() > size().getRows()) {
            int distance = cursor() - size().getRows();
            return this
                    .mapCursor(x -> size().getRows())
                    .mapScroll(scroll -> scroll + distance);
        } else if (row() < 0) {
            return mapScroll(x -> 0);
        } else if (row() > length) {
            int distance = length - row();
            return mapCursor(cursor -> cursor + distance);
        } else {
            return mapText(text -> Text.copyOf(text.normalize()));
        }
    }

    public Search mapText(UnaryOperator<Text> fn) {
        return Search.copyOf(this).withText(fn.apply(text()));
    }

    public Search mapCursor(UnaryOperator<Integer> fn) {
        return Search.copyOf(this).withCursor(fn.apply(cursor()));
    }

    public Search mapScroll(UnaryOperator<Integer> fn) {
        return Search.copyOf(this).withScroll(fn.apply(scroll()));
    }

    public Search mapSearchSpace(UnaryOperator<Stream<Seq<TextCharacter>>> fn) {
        return Search.copyOf(this).withSearchSpace(fn.apply(searchSpace()));
    }

    public Search mapFilter(UnaryOperator<BiFunction<String, String, Boolean>> fn) {
        return Search.copyOf(this).withFilter(fn.apply(filter()));
    }

    public Search mapSize(UnaryOperator<TerminalSize> fn) {
        return Search.copyOf(this).withSize(fn.apply(size()));
    }

    @Override
    public Key type() {
        return key;
    }
}
