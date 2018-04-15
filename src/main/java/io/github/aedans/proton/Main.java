package io.github.aedans.proton;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import fj.data.Seq;
import io.github.aedans.proton.ast.Directory;
import io.github.aedans.proton.logic.Plugins;
import io.github.aedans.proton.logic.Proton;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.ui.TextString;

import java.io.File;

import static com.googlecode.lanterna.TerminalPosition.TOP_LEFT_CORNER;

public final class Main {
    public static void main(String[] args) throws Throwable {
        Plugins.start().run();
        Terminal.start().run();

        Directory home = Directory.from(new File(".")).run();
        Proton proton = new Proton(home, Seq.empty(), -1);

        while (true) {
            try {
                proton = proton.accept(Terminal.read().run());
                proton.render().run();
                Terminal.refresh().run();
            } catch (Throwable t) {
//                t.printStackTrace();
                Seq<TextCharacter> error = TextString.fromString(t.getMessage())
                        .map(x -> x.withForegroundColor(TextColor.ANSI.RED));
                proton.render().run();
                Terminal.clear(TOP_LEFT_CORNER, Terminal.size().run().withRows(1)).run();
                TextString.render(error, TOP_LEFT_CORNER).run();
                Terminal.refresh().run();
            }
        }
    }
}
