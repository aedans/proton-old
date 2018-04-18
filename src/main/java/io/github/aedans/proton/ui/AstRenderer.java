package io.github.aedans.proton.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.util.Unique;
import org.pf4j.ExtensionPoint;

public interface AstRenderer extends ExtensionPoint, Unique {
    Stream<Seq<TextCharacter>> render(Ast ast, TerminalSize size);

    default String text(Ast ast) {
        return TextString.toString(render(ast, new TerminalSize(Integer.MAX_VALUE, Integer.MAX_VALUE)));
    }
}
