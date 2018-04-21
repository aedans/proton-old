package io.github.aedans.proton.system.search;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.system.text.Text;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.AbstractImmutable;
import io.github.aedans.proton.util.Key;
import org.immutables.value.Value;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractSearch implements Ast {
    public static final Key key = Key.unique("text");

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

    @Value.Lazy
    public Stream<Seq<TextCharacter>> filteredSearchSpace() {
        return searchSpace().filter(x -> filter().apply(TextString.toString(x), TextString.toString(text().text())));
    }

    public int row() {
        return cursor() + scroll();
    }

    public Search normalize(TerminalSize size) {
        int length = filteredSearchSpace().take(row() + size.getRows()).length();

        if (cursor() < 0) {
            return this
                    .mapCursor(x -> 0)
                    .withScroll(row())
                    .normalize(size);
        } else if (cursor() > size.getRows()) {
            int distance = cursor() - size.getRows();
            return this
                    .mapCursor(x -> size.getRows())
                    .mapScroll(scroll -> scroll + distance)
                    .normalize(size);
        } else if (row() < 0) {
            return this
                    .mapScroll(x -> 0)
                    .normalize(size);
        } else if (row() > length) {
            int distance = length - row();
            return this
                    .mapCursor(cursor -> cursor + distance)
                    .normalize(size);
        } else {
            return mapText(text -> text.normalize(size));
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

    @Override
    public Key type() {
        return key;
    }
}
