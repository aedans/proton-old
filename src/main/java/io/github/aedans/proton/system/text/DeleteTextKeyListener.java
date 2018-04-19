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
                if (ast.getColumn() <= 0 && ast.getRow() > 0) {
                    return ast
                            .mapLine(ast.getRow() - 1, line -> line.append(ast.getLine(ast.getRow())))
                            .mapText(text -> text.take(ast.getRow()).append(text.drop(ast.getRow() + 1)))
                            .mapCursor(cursor -> cursor
                                    .withRow(ast.cursor.getRow() - 1)
                                    .withColumn(ast.getLine(ast.getRow() - 1).length()));
                } else if (ast.getRow() < 0 || ast.getColumn() <= 0) {
                    return ast;
                } else {
                    return ast
                            .mapLine(ast.getRow(), line -> line.delete(ast.getColumn() - 1))
                            .mapCursor(cursor -> cursor.withRelativeColumn(-1));
                }
            });
        } else {
            return editor;
        }
    }
}