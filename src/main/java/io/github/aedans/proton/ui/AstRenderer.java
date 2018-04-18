package io.github.aedans.proton.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.util.Key;
import io.github.aedans.proton.util.Plugins;
import io.github.aedans.proton.util.Unique;
import org.pf4j.ExtensionPoint;

public interface AstRenderer<A extends Ast> extends ExtensionPoint, Unique {
    Stream<Seq<TextCharacter>> render(A ast, TerminalSize size);

    default String text(A ast) {
        return TextString.toString(render(ast, new TerminalSize(Integer.MAX_VALUE, Integer.MAX_VALUE)));
    }

    @SuppressWarnings("unchecked")
    static <A extends Ast> AstRenderer<A> forKey(Key key) {
        return ((AstRenderer<A>) Plugins.forKey(AstRenderer.class, key).valueE("Could not find ast renderer for " + key));
    }
}
