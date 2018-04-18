package io.github.aedans.proton.system.search;

import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.system.text.TextAst;
import io.github.aedans.proton.util.Key;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public final class SearchAst implements Ast {
    public static final Key type = Key.unique("search");

    public final TextAst search;
    public final Stream<Seq<TextCharacter>> searchSpace;
    public final BiFunction<String, String, Boolean> filter;

    public SearchAst() {
        this(new TextAst(), Stream.nil(), filterContains);
    }

    public SearchAst(
            TextAst search,
            Stream<Seq<TextCharacter>> searchSpace,
            BiFunction<String, String, Boolean> filter
    ) {
        this.search = search;
        this.searchSpace = searchSpace;
        this.filter = filter;
    }

    public SearchAst mapSearch(UnaryOperator<TextAst> fn) {
        return new SearchAst(fn.apply(search), searchSpace, filter);
    }

    public SearchAst mapSearchSpace(UnaryOperator<Stream<Seq<TextCharacter>>> fn) {
        return new SearchAst(search, fn.apply(searchSpace), filter);
    }

    public SearchAst mapFilter(UnaryOperator<BiFunction<String, String, Boolean>> fn) {
        return new SearchAst(search, searchSpace, fn.apply(filter));
    }

    public SearchAst withSearch(TextAst search) {
        return mapSearch(x -> search);
    }

    public SearchAst withSearchSpace(Stream<Seq<TextCharacter>> searchSpace) {
        return mapSearchSpace(x -> searchSpace);
    }

    public SearchAst withFilter(BiFunction<String, String, Boolean> filter) {
        return mapFilter(x -> filter);
    }

    public static final BiFunction<String, String, Boolean> filterContains = String::contains;

    @Override
    public Key type() {
        return type;
    }
}
