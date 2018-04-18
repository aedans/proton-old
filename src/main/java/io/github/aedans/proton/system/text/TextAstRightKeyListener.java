package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class TextAstRightKeyListener implements TextAstKeyListener.Instance {
    @Override
    public Editor apply(Editor editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.ArrowRight))) {
            if (editor.getColumn() >= ((TextAst) editor.ast).getLine(editor.getRow()).length() && editor.getRow() < ((TextAst) editor.ast).lines() - 1) {
                return editor.mapCursor(cursor -> cursor.withRelativeRow(1).withColumn(0));
            } else {
                return editor.mapCursor(cursor -> cursor.withRelativeColumn(1));
            }
        } else {
            return editor;
        }
    }
}
