package io.github.aedans.proton;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import fj.data.Seq;
import io.github.aedans.proton.ast.Directory;
import io.github.aedans.proton.logic.Plugins;
import io.github.aedans.proton.logic.Proton;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.ui.TextString;

import java.io.File;

public final class Main {
    public static void main(String[] args) {
        Plugins.start();
        Terminal.start();

        Directory home = Directory.from(new File("."));
        Proton proton = new Proton(home, Seq.empty(), -1);

        while (true) {
            try {
                proton = proton.accept(Terminal.read());
            } catch (Throwable t) {
                proton.rerender();
                Seq<TextCharacter> error = TextString.fromString("Internal error: " + t.getMessage())
                        .map(x -> x.withForegroundColor(TextColor.ANSI.RED));
                proton.render();
                TextString.render(error, TerminalPosition.TOP_LEFT_CORNER);
                Terminal.refresh();
            }
        }
    }
}
