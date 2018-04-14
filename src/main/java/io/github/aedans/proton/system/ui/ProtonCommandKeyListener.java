package io.github.aedans.proton.system.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import fj.data.List;
import fj.data.Option;
import fj.data.Seq;
import io.github.aedans.proton.logic.Command;
import io.github.aedans.proton.logic.Plugins;
import io.github.aedans.proton.logic.Proton;
import io.github.aedans.proton.ui.Request;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.ui.components.SearchBox;
import io.github.aedans.proton.ui.components.TextBox;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class ProtonCommandKeyListener implements Proton.KeyListener {
    private static final List<Command> allCommands = Plugins.all(Command.class);

    private static List<Command> allCommands(Key type) {
        return allCommands.filter(x -> x.type().equals(Proton.none) || x.type().equals(type));
    }

    @Override
    public Proton apply(Proton proton, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() >= 'a' && keyStroke.getCharacter() <= 'z') {
            String name = new Request()
                    .withBackground(proton)
                    .withComponent(new SearchBox()
                            .withTextBox(new TextBox()
                                    .withText(Seq.single(Seq.single(new TextCharacter(keyStroke.getCharacter()))))
                                    .withCursor(TerminalPosition.TOP_LEFT_CORNER.withRelativeColumn(1)))
                            .withMax(5)
                            .withSearch(allCommands(proton.focusDisplayType()).toStream().map(x -> TextString.fromString(x.command()))))
                    .withEnd(Terminal.identifier)
                    .run();
            Option<Command> command = allCommands(proton.focusDisplayType()).find(x -> x.command().equals(name));
            try {
                return command.valueE(() -> "Could not find command " + name).apply(proton).rerender();
            } catch (Throwable t) {
                String message = "Error executing command " + name + ": " + t.getMessage();
                Seq<TextCharacter> error = TextString.fromString(message).map(x -> x.withForegroundColor(TextColor.ANSI.RED));
                proton.render();
                TextString.render(error, TerminalPosition.TOP_LEFT_CORNER);
                Terminal.refresh();
                return proton;
            }
        } else {
            return proton;
        }
    }
}
