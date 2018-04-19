package io.github.aedans.proton.system.java;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.KeyListener;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class JavaAstKeyListener implements KeyListener<JavaAst> {
    @Override
    public Editor<JavaAst> apply(Editor<JavaAst> editor, KeyStroke keyStroke) {
        return editor;
    }

    @Override
    public Key key() {
        return JavaAst.key;
    }
}
