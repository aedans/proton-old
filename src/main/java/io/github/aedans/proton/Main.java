package io.github.aedans.proton;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import fj.data.Seq;
import io.github.aedans.proton.system.directory.Directory;
import io.github.aedans.proton.util.Plugins;
import io.github.aedans.proton.system.proton.Proton;
import io.github.aedans.proton.ui.Editor;
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
        Editor editor = new Editor<>(proton);

        while (true) {
            try {
                editor = editor.accept(Terminal.read().run());
                editor.render().run();
                Terminal.refresh().run();
            } catch (Throwable t) {
                t.printStackTrace();
                Seq<TextCharacter> error = TextString.fromString(t.getMessage())
                        .map(x -> x.withForegroundColor(TextColor.ANSI.RED));
                editor.render().run();
                Terminal.clear(TOP_LEFT_CORNER, Terminal.size().run().withRows(1)).run();
                TextString.render(error, TOP_LEFT_CORNER).run();
                Terminal.refresh().run();
            }
        }
    }
}
