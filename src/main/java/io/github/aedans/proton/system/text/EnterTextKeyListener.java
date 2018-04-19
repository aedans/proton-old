package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import fj.data.Seq;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class EnterTextKeyListener implements TextKeyListener.Instance {
    @Override
    public Editor<Text> apply(Editor<Text> editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.Enter))) {
            return editor.mapAst(ast -> {
                Seq<Seq<TextCharacter>> text = ast.text;
                Seq<TextCharacter> line = text.index(ast.getRow());
                Seq<TextCharacter> before = line.take(ast.getColumn());
                Seq<TextCharacter> after = line.drop(ast.getColumn());
                Seq<Seq<TextCharacter>> newText = text
                        .delete(ast.getRow())
                        .insert(ast.getRow(), after)
                        .insert(ast.getRow(), before);
                return ast
                        .withText(newText)
                        .mapCursor(cursor -> cursor.withRelativeRow(1).withColumn(0))
                        .mapScroll(scroll -> scroll.withColumns(0));
            });
        } else {
            return editor;
        }
    }
}
