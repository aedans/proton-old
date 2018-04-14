package io.github.aedans.proton.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;

public interface Component {
    Component accept(KeyStroke keyStroke);

    void render(TerminalPosition offset, TerminalSize size);

    default void render() {
        render(TerminalPosition.TOP_LEFT_CORNER, Terminal.size());
    }
}
