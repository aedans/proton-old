package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class CharacterTextKeyListener implements TextKeyListener.Instance {
    @Override
    public Editor<Text> apply(Editor<Text> editor, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Character && !keyStroke.isAltDown() && !keyStroke.isCtrlDown()) {
            return editor.mapAst(ast -> ast
                    .mapLine(editor.ast.getRow(), line -> line.insert(ast.getColumn(), new TextCharacter(keyStroke.getCharacter())))
                    .mapCursor(cursor -> cursor.withRelativeColumn(1)));
        } else {
            return editor;
        }
    }
}
