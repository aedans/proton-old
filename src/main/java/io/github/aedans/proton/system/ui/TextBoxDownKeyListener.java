package io.github.aedans.proton.system.ui;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.components.TextBox;
import org.pf4j.Extension;

@Extension
public final class TextBoxDownKeyListener implements TextBox.KeyListener {
    @Override
    public TextBox apply(TextBox textBox, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.ArrowDown))) {
            return textBox.mapCursor(cursor -> cursor.withRelativeRow(1));
        } else {
            return textBox;
        }
    }
}
