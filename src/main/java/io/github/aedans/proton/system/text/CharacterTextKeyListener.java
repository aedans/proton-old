package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import fj.data.Seq;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class CharacterTextKeyListener implements TextKeyListener.Instance {
    @Override
    public Editor<Text> apply(Editor<Text> editor, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Character && !keyStroke.isAltDown() && !keyStroke.isCtrlDown()) {
            Seq<TextCharacter> line = editor.ast.text.index(editor.getRow());
            return editor
                    .mapAst(ast -> {
                        Seq<Seq<TextCharacter>> newText = ast.text.update(
                                editor.getRow(),
                                line.insert(editor.getColumn(), new TextCharacter(keyStroke.getCharacter()))
                        );
                        return new Text(newText);
                    })
                    .mapCursor(cursor -> cursor.withRelativeColumn(1));
        } else {
            return editor;
        }
    }
}
