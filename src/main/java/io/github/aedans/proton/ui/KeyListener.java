package io.github.aedans.proton.ui;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.aedans.proton.util.Unique;
import org.pf4j.ExtensionPoint;

public interface KeyListener extends ExtensionPoint, Unique {
    Editor apply(Editor editor, KeyStroke keyStroke);
}
