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
                if (ast.column() <= 0 && ast.row() > 0) {
                    return ast.mapCursor(cursor -> cursor
                            .withRow(ast.cursor().getRow() - 1)
                            .withColumn(ast.line(ast.row() - 1).length()))
                            .mapScroll(scroll -> scroll.withColumns(0));
                } else {
                    return ast
                            .mapCursor(cursor -> cursor.withRelativeColumn(-1));
                }
            });
        } else {
            return editor;
        }
    }
}
