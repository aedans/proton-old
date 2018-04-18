package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import fj.data.Seq;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class TextAstEnterKeyListener implements TextAstKeyListener.Instance {
    @Override
    public Editor apply(Editor editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.Enter))) {
            return editor
                    .mapAst(ast -> {
                        Seq<Seq<TextCharacter>> text = ((TextAst) ast).text;
                        Seq<TextCharacter> line = text.index(editor.getRow());
                        Seq<TextCharacter> before = line.take(editor.getColumn());
                        Seq<TextCharacter> after = line.drop(editor.getColumn());
                        Seq<Seq<TextCharacter>> newText = text
                                .delete(editor.getRow())
                                .insert(editor.getRow(), after)
                                .insert(editor.getRow(), before);
                        return new TextAst(newText);
                    })
                    .mapCursor(cursor -> cursor.withRelativeRow(1).withColumn(0))
                    .mapScroll(scroll -> scroll.withColumns(0));
        } else {
            return editor;
        }
    }
}
