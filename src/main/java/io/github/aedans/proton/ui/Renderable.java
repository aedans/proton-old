package io.github.aedans.proton.ui;

import com.googlecode.lanterna.TerminalPosition;
import fj.Unit;
import io.github.aedans.pfj.IO;

import static com.googlecode.lanterna.TerminalPosition.TOP_LEFT_CORNER;

public interface Renderable {
    IO<Unit> render(TerminalPosition offset);

    default IO<Unit> render() {
        return Terminal.size().flatMap(size -> render(TOP_LEFT_CORNER));
    }
}
