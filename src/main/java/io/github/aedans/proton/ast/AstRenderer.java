package io.github.aedans.proton.ast;

import com.googlecode.lanterna.TerminalSize;
import io.github.aedans.proton.ui.AstRendererResult;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import io.github.aedans.proton.util.Plugins;
import io.github.aedans.proton.util.Unique;
import org.pf4j.ExtensionPoint;

public interface AstRenderer<A extends Ast> extends ExtensionPoint, Unique {
    @SuppressWarnings("unchecked")
    static <A extends Ast> AstRenderer<A> forKey(Key key) {
        return ((AstRenderer<A>) Plugins.forKey(AstRenderer.class, key).valueE("Could not find ast renderer for " + key));
    }

    AstRendererResult render(A ast, TerminalSize size);

    default String entry(A ast) {
        AstRendererResult rendererResult = render(ast, new TerminalSize(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return TextString.toString(rendererResult.text());
    }
}
