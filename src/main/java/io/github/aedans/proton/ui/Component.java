package io.github.aedans.proton.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import fj.Unit;
import io.github.aedans.pfj.IO;

import static com.googlecode.lanterna.TerminalPosition.TOP_LEFT_CORNER;

public interface Component {
    Component accept(KeyStroke keyStroke);

    IO<Unit> render(TerminalPosition offset, TerminalSize size);

    default IO<Unit> render() {
        return Terminal.size().flatMap(size -> render(TOP_LEFT_CORNER, size));
    }
}
