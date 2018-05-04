package io.github.aedans.proton.system.json;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public final class BackspaceJsonKeyListener implements JsonKeyListener.Instance {
    @Override
    public Editor<JsonAst> apply(Editor<JsonAst> editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.Backspace))) {
            return editor.mapAst(ast -> {
                if (ast instanceof AbstractJsonObjectAst) {
                    AbstractJsonObjectAst jsonObjectAst = (AbstractJsonObjectAst) ast;
                    return jsonObjectAst.delete(jsonObjectAst.selectedName());
                } else {
                    return ast;
                }
            });
        } else {
            return editor;
        }
    }
}
