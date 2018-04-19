package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.input.KeyStroke;
import fj.data.List;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.KeyListener;
import io.github.aedans.proton.util.Key;
import io.github.aedans.proton.util.Plugins;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;

@Extension
public final class TextKeyListener implements KeyListener<Text> {
    public static final List<Instance> instances = Plugins.all(Instance.class);

    @Override
    public Editor<Text> apply(Editor<Text> editor, KeyStroke keyStroke) {
        return instances.foldLeft((a, b) -> b.apply(a, keyStroke), editor)
                .mapAst(ast -> ast.normalize(editor.size));
    }

    @Override
    public Key key() {
        return Text.key;
    }

    public interface Instance extends ExtensionPoint {
        Editor<Text> apply(Editor<Text> editor, KeyStroke keyStroke);
    }
}
