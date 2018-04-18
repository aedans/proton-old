package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.input.KeyStroke;
import fj.data.List;
import io.github.aedans.proton.util.Plugins;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.KeyListener;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;

@Extension
public final class TextAstKeyListener implements KeyListener {
    public static final List<Instance> instances = Plugins.all(Instance.class);

    @Override
    public Editor apply(Editor editor, KeyStroke keyStroke) {
        return instances.foldLeft((a, b) -> b.apply(a, keyStroke), editor);
    }

    @Override
    public Key key() {
        return TextAst.key;
    }

    public interface Instance extends ExtensionPoint {
        Editor apply(Editor editor, KeyStroke keyStroke);
    }
}
