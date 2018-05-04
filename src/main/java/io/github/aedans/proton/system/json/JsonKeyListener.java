package io.github.aedans.proton.system.json;

import com.googlecode.lanterna.input.KeyStroke;
import fj.data.List;
import io.github.aedans.proton.system.text.TextKeyListener;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.KeyListener;
import io.github.aedans.proton.util.Key;
import io.github.aedans.proton.util.Plugins;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;

@Extension
public final class JsonKeyListener implements KeyListener<JsonAst> {
    public static final List<Instance> instances = Plugins.all(Instance.class);

    @Override
    public Editor<JsonAst> apply(Editor<JsonAst> editor, KeyStroke keyStroke) {
        return instances.foldLeft((a, b) -> b.apply(a, keyStroke), editor);
    }

    @Override
    public Key key() {
        return JsonObjectAst.key;
    }

    public interface Instance extends ExtensionPoint {
        Editor<JsonAst> apply(Editor<JsonAst> editor, KeyStroke keyStroke);
    }
}
