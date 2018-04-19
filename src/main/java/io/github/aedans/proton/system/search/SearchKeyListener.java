package io.github.aedans.proton.system.search;

import com.googlecode.lanterna.input.KeyStroke;
import fj.data.List;
import io.github.aedans.proton.system.text.TextKeyListener;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.KeyListener;
import io.github.aedans.proton.util.Key;
import io.github.aedans.proton.util.Plugins;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;

@Extension
public final class SearchKeyListener implements KeyListener<Search> {
    public static final List<Instance> instances = Plugins.all(Instance.class)
            .append(TextKeyListener.instances.map(textKeyListener ->
                    (Instance) (editor, keyStroke) ->
                            textKeyListener.apply(editor.map(x -> x.text), keyStroke).map(editor.ast::withText)));

    @Override
    public Editor<Search> apply(Editor<Search> editor, KeyStroke keyStroke) {
        return instances.foldLeft((a, b) -> b.apply(a, keyStroke), editor);
    }

    @Override
    public Key key() {
        return Search.key;
    }

    public interface Instance extends ExtensionPoint {
        Editor<Search> apply(Editor<Search> editor, KeyStroke keyStroke);
    }
}
