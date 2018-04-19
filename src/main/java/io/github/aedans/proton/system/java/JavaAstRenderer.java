package io.github.aedans.proton.system.java;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ui.AstRenderer;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class JavaAstRenderer implements AstRenderer<JavaAst> {
    @Override
    public Stream<Seq<TextCharacter>> render(JavaAst ast, TerminalSize size) {
        return new JavaAstWriter().write(ast).map(TextString::fromString);
    }

    @Override
    public TerminalPosition cursor(JavaAst ast, TerminalSize size) {
        return TerminalPosition.TOP_LEFT_CORNER;
    }

    @Override
    public Key key() {
        return JavaAst.key;
    }
}
