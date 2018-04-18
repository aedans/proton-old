package io.github.aedans.proton.system.search;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.ast.Directory;
import io.github.aedans.proton.ui.AstRenderer;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class SearchAstRenderer implements AstRenderer {
    @Override
    public Stream<Seq<TextCharacter>> render(Ast ast, TerminalSize size) {
        SearchAst searchAst = (SearchAst) ast;

        Stream<Seq<TextCharacter>> matches = searchAst.searchSpace
                .filter(x -> searchAst.filter.apply(TextString.toString(x), TextString.toString(searchAst.search.text.toStream())));

        Seq<TextCharacter> text = searchAst.search.text.toStream().foldLeft1(Seq::append);

        return matches.cons(text);
    }

    @Override
    public String text(Ast ast) {
        return TextString.toString(((SearchAst) ast).search.text.toStream());
    }

    @Override
    public Key key() {
        return SearchAst.type;
    }
}
