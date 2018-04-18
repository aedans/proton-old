package io.github.aedans.proton.system.directory;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.aedans.proton.ast.Directory;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.KeyListener;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class DirectoryKeyListener implements KeyListener<Directory> {
    @Override
    public Editor<Directory> apply(Editor<Directory> editor, KeyStroke keyStroke) {
        return editor;
    }

    @Override
    public Key key() {
        return Directory.key;
    }
}
