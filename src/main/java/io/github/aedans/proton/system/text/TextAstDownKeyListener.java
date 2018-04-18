package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class TextAstDownKeyListener implements TextAstKeyListener.Instance {
    @Override
    public Editor apply(Editor editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.ArrowDown))) {
            return editor.mapCursor(cursor -> cursor.withRelativeRow(1));
        } else {
            return editor;
        }
    }
}
