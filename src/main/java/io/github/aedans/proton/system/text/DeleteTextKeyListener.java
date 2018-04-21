package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class DeleteTextKeyListener implements TextKeyListener.Instance {
    @Override
    public Editor<Text> apply(Editor<Text> editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.Backspace))) {
            return editor.mapAst(ast -> {
                if (ast.column() <= 0 && ast.row() > 0) {
                    return ast
                            .mapLine(ast.row() - 1, line -> line.append(ast.line(ast.row())))
                            .mapText(text -> text.take(ast.row()).append(text.drop(ast.row() + 1)))
                            .mapCursor(cursor -> cursor
                                    .withRow(ast.cursor().getRow() - 1)
                                    .withColumn(ast.line(ast.row() - 1).length()));
                } else if (ast.row() < 0 || ast.column() <= 0) {
                    return ast;
                } else {
                    return ast
                            .mapLine(ast.row(), line -> line.delete(ast.column() - 1))
                            .mapCursor(cursor -> cursor.withRelativeColumn(-1));
                }
            });
        } else {
            return editor;
        }
    }
}