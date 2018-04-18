package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class TextAstLeftKeyListener implements TextAstKeyListener.Instance {
    @Override
    public Editor apply(Editor editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.ArrowLeft))) {
            if (editor.getColumn() <= 0 && editor.getRow() > 0) {
                return editor
                        .mapCursor(cursor -> cursor
                        .withRow(editor.cursor.getRow() - 1)
                        .withColumn(((TextAst) editor.ast).getLine(editor.getRow() - 1).length()));
            } else {
                return editor.mapCursor(cursor -> cursor.withRelativeColumn(-1));
            }
        } else {
            return editor;
        }
    }
}
