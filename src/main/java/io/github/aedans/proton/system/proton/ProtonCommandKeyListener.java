package io.github.aedans.proton.system.proton;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import fj.data.List;
import fj.data.Seq;
import io.github.aedans.proton.util.Plugins;
import io.github.aedans.proton.system.search.Search;
import io.github.aedans.proton.system.text.Text;
import io.github.aedans.proton.ui.Request;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class ProtonCommandKeyListener implements ProtonKeyListener.Instance {
    private static final List<Command> allCommands = Plugins.all(Command.class);

    private static List<Command> allCommands(Key type) {
        return allCommands.filter(x -> x.type().equals(Proton.none) || x.type().equals(type));
    }

    @Override
    public Editor<Proton> apply(Editor<Proton> editor, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() >= 'a' && keyStroke.getCharacter() <= 'z') {
            List<Command> commands = allCommands(editor.ast.getFocusedEditorType());
            Text text = new Text(Seq.single(Seq.single(new TextCharacter(keyStroke.getCharacter()))))
                    .withCursor(TerminalPosition.TOP_LEFT_CORNER.withRelativeColumn(1));
            Search search = new Search()
                    .withText(text)
                    .withSearchSpace(commands.toStream().map(x -> TextString.fromString(x.command())));
            Editor<Search> searchEditor = new Editor<>(search);
            return new Request()
                    .withBackground(editor)
                    .withEditor(searchEditor)
                    .withEnd(Terminal.identifier)
                    .run()
                    .flatMap(name -> commands.find(x -> x.command().equals(name))
                            .valueE(() -> "Could not find command " + name)
                            .apply(editor.ast))
                    .map(Editor::new)
                    .runUnsafe();
        } else {
            return editor;
        }
    }
}
