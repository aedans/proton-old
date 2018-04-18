package io.github.aedans.proton.system.proton;

import com.googlecode.lanterna.input.KeyStroke;
import fj.data.List;
import io.github.aedans.proton.util.Plugins;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.KeyListener;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;

@Extension
public final class ProtonKeyListener implements KeyListener<Proton> {
    private static final List<Instance> keyListeners = Plugins.all(Instance.class);

    @Override
    public Editor<Proton> apply(Editor<Proton> editor, KeyStroke keyStroke) {
//        Option<Editor> selectedEditor = ((Proton) editor.ast).getFocusedEditor();
        return keyListeners.foldLeft((a, b) -> b.apply(a, keyStroke), editor);
    }

    @Override
    public Key key() {
        return Proton.key;
    }

    public interface Instance extends ExtensionPoint {
        Editor<Proton> apply(Editor<Proton> editor, KeyStroke keyStroke);
    }
}
