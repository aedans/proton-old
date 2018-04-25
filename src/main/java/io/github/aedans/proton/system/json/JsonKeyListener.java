package io.github.aedans.proton.system.json;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.KeyListener;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class JsonKeyListener implements KeyListener<JsonObjectAst> {
    @Override
    public Editor<JsonObjectAst> apply(Editor<JsonObjectAst> editor, KeyStroke keyStroke) {
        return editor;
    }

    @Override
    public Key key() {
        return JsonAst.key;
    }
}
