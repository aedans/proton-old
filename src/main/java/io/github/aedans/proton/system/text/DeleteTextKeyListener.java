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
            if (editor.getColumn() <= 0 && editor.getRow() > 0) {
                return editor
                        .mapCursor(cursor -> cursor
                                .withRow(editor.cursor.getRow() - 1)
                                .withColumn(editor.ast.getLine(editor.getRow() - 1).length()))
                        .mapAst(ast -> {
                            Text textAst = ast;
                            return textAst
                                    .mapLine(editor.getRow() - 1, line -> line.append(textAst.getLine(editor.getRow())))
                                    .mapText(text -> text.delete(editor.getRow()));
                        });
            } else if (editor.getRow() < 0 || editor.getColumn() <= 0) {
                return editor;
            } else {
                return editor
                        .mapCursor(cursor -> cursor.withRelativeColumn(-1))
                        .mapAst(ast -> ast.mapLine(editor.getRow(), line -> line.delete(editor.getColumn() - 1)));
            }
        } else {
            return editor;
        }
    }
}