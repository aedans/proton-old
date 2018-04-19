package io.github.aedans.proton.system.proton;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import fj.data.List;
import fj.data.Option;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.KeyListener;
import io.github.aedans.proton.util.Key;
import io.github.aedans.proton.util.Plugins;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;

@Extension
public final class ProtonKeyListener implements KeyListener<Proton> {
    private static final List<Instance> keyListeners = Plugins.all(Instance.class);

    @Override
    public Editor<Proton> apply(Editor<Proton> editor, KeyStroke keyStroke) {
        Option<Integer> focusedEditorIndex = editor.ast.getFocusedEditorIndex();
        if (keyStroke.equals(new KeyStroke(KeyType.Escape))) {
            return editor.mapAst(ast ->
                    ast.withFocused(false));
        } else if (focusedEditorIndex.isSome()) {
            Editor newEditor = editor.ast.editors.index(focusedEditorIndex.some()).accept(keyStroke);
            return editor.mapAst(ast ->
                    ast.mapEditors(editors ->
                            editors.update(focusedEditorIndex.some(), newEditor)));
        } else {
            return keyListeners.foldLeft((a, b) -> b.apply(a, keyStroke), editor);
        }
    }

    @Override
    public Key key() {
        return Proton.key;
    }

    public interface Instance extends ExtensionPoint {
        Editor<Proton> apply(Editor<Proton> editor, KeyStroke keyStroke);
    }
}
