package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.ui.AstRenderer;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class TextAstRenderer implements AstRenderer {
    @Override
    public Key key() {
        return TextAst.key;
    }

    @Override
    public Stream<Seq<TextCharacter>> render(Ast ast, TerminalSize size) {
        return ((TextAst) ast).text.toStream();
    }
}
