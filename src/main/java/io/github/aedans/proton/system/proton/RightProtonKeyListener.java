package io.github.aedans.proton.system.proton;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class RightProtonKeyListener implements ProtonKeyListener.Instance {
    @Override
    public Editor<Proton> apply(Editor<Proton> editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.ArrowRight))) {
            return editor.mapAst(ast -> ast.mapSelected(selected -> selected + 1));
        } else {
            return editor;
        }
    }
}