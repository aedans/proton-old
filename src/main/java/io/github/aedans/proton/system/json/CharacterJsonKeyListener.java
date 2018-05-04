package io.github.aedans.proton.system.json;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.system.text.Text;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.Request;
import io.github.aedans.proton.ui.Terminal;
import org.pf4j.Extension;

@Extension
public final class CharacterJsonKeyListener implements JsonKeyListener.Instance {
    @Override
    public Editor<JsonAst> apply(Editor<JsonAst> editor, KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.Character)) {
            Text text = Text.builder()
                    .text(Stream.single(Seq.single(new TextCharacter(keyStroke.getCharacter()))))
                    .cursor(TerminalPosition.TOP_LEFT_CORNER.withRelativeColumn(1))
                    .build();
            Editor<Text> searchEditor = Editor.<Text>builder().ast(text).build();
            return Request.builder()
                    .background(editor)
                    .editor(searchEditor)
                    .end(Terminal.identifier)
                    .build()
                    .run()
                    .map(name -> {
                        return editor.mapAst(ast -> {
                            if (ast instanceof AbstractJsonObjectAst) {
                                return ((AbstractJsonObjectAst) ast).with(name, JsonObjectAst.builder().build());
                            } else {
                                return ast;
                            }
                        });
                    })
                    .runUnsafe();
        } else {
            return editor;
        }
    }
}
