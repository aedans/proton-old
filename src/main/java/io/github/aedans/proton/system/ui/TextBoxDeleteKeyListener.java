package io.github.aedans.proton.system.ui;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.components.TextBox;
import org.pf4j.Extension;

@Extension
public final class TextBoxDeleteKeyListener implements TextBox.KeyListener {
    @Override
    public TextBox apply(TextBox textBox, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.Backspace))) {
            if (textBox.getColumn() <= 0 && textBox.getRow() > 0) {
                return textBox
                        .mapCursor(cursor -> cursor
                                .withRow(textBox.cursor.getRow() - 1)
                                .withColumn(textBox.getLine(textBox.getRow() - 1).length()))
                        .mapLine(textBox.getRow() - 1, line -> line.append(textBox.getLine(textBox.getRow())))
                        .mapText(text -> text.delete(textBox.getRow()));
            } else if (textBox.getRow() < 0 || textBox.getColumn() <= 0) {
                return textBox;
            } else {
                return textBox
                        .mapCursor(cursor -> cursor.withRelativeColumn(-1))
                        .mapLine(textBox.getRow(), line -> line.delete(textBox.getColumn() - 1));
            }
        } else {
            return textBox;
        }
    }
}
