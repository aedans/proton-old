package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class LeftTextKeyListener implements TextKeyListener.Instance {
    @Override
    public Editor<Text> apply(Editor<Text> editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.ArrowLeft))) {
            return editor.mapAst(ast -> {
                if (ast.getColumn() <= 0 && ast.getRow() > 0) {
                    return ast.mapCursor(cursor -> cursor
                            .withRow(ast.cursor.getRow() - 1)
                            .withColumn(ast.getLine(ast.getRow() - 1).length()));
                } else {
                    return ast.mapCursor(cursor -> cursor.withRelativeColumn(-1));
                }
            });
        } else {
            return editor;
        }
    }
}
