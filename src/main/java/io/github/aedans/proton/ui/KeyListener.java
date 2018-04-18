package io.github.aedans.proton.ui;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.util.Key;
import io.github.aedans.proton.util.Plugins;
import io.github.aedans.proton.util.Unique;
import org.pf4j.ExtensionPoint;

public interface KeyListener<A extends Ast> extends ExtensionPoint, Unique {
    Editor<A> apply(Editor<A> editor, KeyStroke keyStroke);

    @SuppressWarnings("unchecked")
    static <A extends Ast> KeyListener<A> forKey(Key key) {
        return ((KeyListener<A>) Plugins.forKey(KeyListener.class, key).valueE("Could not find key listener for " + key));
    }
}
