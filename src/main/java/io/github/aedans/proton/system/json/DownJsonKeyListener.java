package io.github.aedans.proton.system.json;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class DownJsonKeyListener implements JsonKeyListener.Instance {
    @Override
    public Editor<JsonAst> apply(Editor<JsonAst> editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.ArrowDown))) {
            return editor.mapAst(ast -> {
                if (ast instanceof JsonObjectAst) {
                    JsonObjectAst jsonObjectAst = (JsonObjectAst) ast;
                    return jsonObjectAst.withSelected(jsonObjectAst.selected() + 1);
                } else {
                    return ast;
                }
            });
        } else {
            return editor;
        }
    }
}
