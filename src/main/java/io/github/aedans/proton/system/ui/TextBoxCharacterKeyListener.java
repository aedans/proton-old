package io.github.aedans.proton.system.ui;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import fj.data.Seq;
import io.github.aedans.proton.ui.components.TextBox;
import org.pf4j.Extension;

@Extension
public final class TextBoxCharacterKeyListener implements TextBox.KeyListener {
    @Override
    public TextBox apply(TextBox textBox, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Character && !keyStroke.isAltDown() && !keyStroke.isCtrlDown()) {
            Seq<TextCharacter> line = textBox.text.index(textBox.getRow());
            return textBox
                    .mapText(text -> text.update(textBox.getRow(), line.insert(textBox.getColumn(), new TextCharacter(keyStroke.getCharacter()))))
                    .mapCursor(cursor -> cursor.withRelativeColumn(1));
        } else {
            return textBox;
        }
    }
}
