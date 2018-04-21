package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class RightTextKeyListener implements TextKeyListener.Instance {
    @Override
    public Editor<Text> apply(Editor<Text> editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.ArrowRight))) {
            return editor.mapAst(ast -> {
                if (ast.column() >= ast.line(ast.row()).length() && ast.row() < ast.lines() - 1) {
                    return ast
                            .mapCursor(cursor -> cursor.withRelativeRow(1).withColumn(0))
                            .mapScroll(scroll -> scroll.withColumns(0));
                } else {
                    return ast.mapCursor(cursor -> cursor.withRelativeColumn(1));
                }
            });
        } else {
            return editor;
        }
    }
}
