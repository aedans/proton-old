package io.github.aedans.proton.system.proton;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class EnterProtonKeyListener implements ProtonKeyListener.Instance {
    @Override
    public Editor<Proton> apply(Editor<Proton> editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.Enter))) {
            return editor.mapAst(ast -> ast.withFocused(true));
        } else {
            return editor;
        }
    }
}
