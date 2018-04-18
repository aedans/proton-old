package io.github.aedans.proton.system.search;

import com.googlecode.lanterna.input.KeyStroke;
import fj.data.List;
import io.github.aedans.proton.util.Plugins;
import io.github.aedans.proton.system.text.TextAst;
import io.github.aedans.proton.system.text.TextAstKeyListener;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.KeyListener;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;

@Extension
public final class SearchAstKeyListener implements KeyListener {
    public static final List<Instance> instances = Plugins.all(Instance.class)
            .append(TextAstKeyListener.instances.map(text ->
                    (Instance) (display, keyStroke) -> text.apply(display.mapAst(ast -> ((SearchAst) ast).search), keyStroke)
                            .mapAst(ast -> ((SearchAst) display.ast).withSearch(((TextAst) ast)))));

    @Override
    public Editor apply(Editor editor, KeyStroke keyStroke) {
        return instances.foldLeft((a, b) -> b.apply(a, keyStroke), editor);
    }

    @Override
    public Key key() {
        return SearchAst.type;
    }

    public interface Instance extends ExtensionPoint {
        Editor apply(Editor display, KeyStroke keyStroke);
    }
}
