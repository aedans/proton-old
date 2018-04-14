package io.github.aedans.proton.system.ui;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.components.TextBox;
import org.pf4j.Extension;

@Extension
public final class TextBoxRightKeyListener implements TextBox.KeyListener {
    @Override
    public TextBox apply(TextBox textBox, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.ArrowRight))) {
            if (textBox.getColumn() >= textBox.getLine(textBox.getRow()).length() && textBox.getRow() < textBox.lines() - 1) {
                return textBox.mapCursor(cursor -> cursor.withRelativeRow(1).withColumn(0));
            } else {
                return textBox.mapCursor(cursor -> cursor.withRelativeColumn(1));
            }
        } else {
            return textBox;
        }
    }
}
