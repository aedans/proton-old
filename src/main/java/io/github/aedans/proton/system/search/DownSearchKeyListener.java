package io.github.aedans.proton.system.search;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.ui.Editor;
import org.pf4j.Extension;

@Extension
public class DownSearchKeyListener implements SearchKeyListener.Instance {
    @Override
    public Editor<Search> apply(Editor<Search> editor, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.ArrowDown)))
            return editor.mapAst(ast -> ast.mapCursor(cursor -> cursor + 1));
        else
            return editor;
    }
}
