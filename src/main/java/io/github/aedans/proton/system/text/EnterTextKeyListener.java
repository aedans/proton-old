package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class EnterTextKeyListener implements TextKeyListener.Instance {
    @Override
    public Editor<Text> apply(Editor<Text> editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.Enter))) {
            return editor.mapAst(ast -> {
                Seq<TextCharacter> line = ast.text().index(ast.row());
                Seq<TextCharacter> before = line.take(ast.column());
                Seq<TextCharacter> after = line.drop(ast.column());
                Stream<Seq<TextCharacter>> newText = ast.text()
                        .take(ast.row())
                        .snoc(before)
                        .snoc(after)
                        .append(ast.text().drop(ast.row() + 1));
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
