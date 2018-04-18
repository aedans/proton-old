package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import fj.data.Seq;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class TextAstCharacterKeyListener implements TextAstKeyListener.Instance {
    @Override
    public Editor apply(Editor editor, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Character && !keyStroke.isAltDown() && !keyStroke.isCtrlDown()) {
            Seq<TextCharacter> line = ((TextAst) editor.ast).text.index(editor.getRow());
            return editor
                    .mapAst(ast -> {
                        Seq<Seq<TextCharacter>> text = ((TextAst) ast).text;
                        Seq<Seq<TextCharacter>> newText = text.update(
                                editor.getRow(),
                                line.insert(editor.getColumn(), new TextCharacter(keyStroke.getCharacter()))
                        );
                        return new TextAst(newText);
                    })
                    .mapCursor(cursor -> cursor.withRelativeColumn(1));
        } else {
            return editor;
        }
    }
}
