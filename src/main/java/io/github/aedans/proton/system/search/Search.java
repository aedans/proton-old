package io.github.aedans.proton.system.search;

import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.system.text.Text;
import io.github.aedans.proton.util.Key;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public final class Search implements Ast {
    public static final Key key = Key.unique("search");

    public final Text search;
    public final Stream<Seq<TextCharacter>> searchSpace;
    public final BiFunction<String, String, Boolean> filter;

    public Search() {
        this(new Text(), Stream.nil(), filterContains);
    }

    public Search(
            Text search,
            Stream<Seq<TextCharacter>> searchSpace,
            BiFunction<String, String, Boolean> filter
    ) {
        this.search = search;
        this.searchSpace = searchSpace;
        this.filter = filter;
    }

    public Search mapSearch(UnaryOperator<Text> fn) {
        return new Search(fn.apply(search), searchSpace, filter);
    }

    public Search mapSearchSpace(UnaryOperator<Stream<Seq<TextCharacter>>> fn) {
        return new Search(search, fn.apply(searchSpace), filter);
    }

    public Search mapFilter(UnaryOperator<BiFunction<String, String, Boolean>> fn) {
        return new Search(search, searchSpace, fn.apply(filter));
    }

    public Search withSearch(Text search) {
        return mapSearch(x -> search);
    }

    public Search withSearchSpace(Stream<Seq<TextCharacter>> searchSpace) {
        return mapSearchSpace(x -> searchSpace);
    }

    public Search withFilter(BiFunction<String, String, Boolean> filter) {
        return mapFilter(x -> filter);
    }

    public static final BiFunction<String, String, Boolean> filterContains = String::contains;

    @Override
    public Key type() {
        return key;
    }
}