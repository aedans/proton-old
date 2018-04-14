package io.github.aedans.proton.system.ui;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import fj.data.Seq;
import io.github.aedans.proton.ui.components.TextBox;
import org.pf4j.Extension;

@Extension
public final class TextBoxEnterKeyListener implements TextBox.KeyListener {
    @Override
    public TextBox apply(TextBox textBox, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.Enter))) {
            return textBox
                    .mapText(text -> {
                        Seq<TextCharacter> line = text.index(textBox.getRow());
                        Seq<TextCharacter> before = line.take(textBox.getColumn());
                        Seq<TextCharacter> after = line.drop(textBox.getColumn());
                        return text
                                .delete(textBox.getRow())
                                .insert(textBox.getRow(), after)
                                .insert(textBox.getRow(), before);
                    })
                    .mapCursor(cursor -> cursor.withRelativeRow(1).withColumn(0))
                    .mapScroll(scroll -> scroll.withColumns(0));
        } else {
            return textBox;
        }
    }
}
