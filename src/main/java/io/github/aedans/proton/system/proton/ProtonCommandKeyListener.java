package io.github.aedans.proton.system.proton;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import fj.data.List;
import fj.data.Seq;
import io.github.aedans.proton.util.Plugins;
import io.github.aedans.proton.system.search.SearchAst;
import io.github.aedans.proton.system.text.TextAst;
import io.github.aedans.proton.ui.Request;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class ProtonCommandKeyListener implements ProtonAstKeyListener.Instance {
    private static final List<Command> allCommands = Plugins.all(Command.class);

    private static List<Command> allCommands(Key type) {
        return allCommands.filter(x -> x.type().equals(Proton.none) || x.type().equals(type));
    }

    @Override
    public Editor apply(Editor editor, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() >= 'a' && keyStroke.getCharacter() <= 'z') {
            List<Command> commands = allCommands(((Proton) editor.ast).getFocusedEditorType());
            SearchAst searchAst = new SearchAst()
                    .withSearch(new TextAst(Seq.single(Seq.single(new TextCharacter(keyStroke.getCharacter())))))
                    .withSearchSpace(commands.toStream().map(x -> TextString.fromString(x.command())));
            Editor searchEditor = new Editor(searchAst).withCursor(TerminalPosition.TOP_LEFT_CORNER.withRelativeColumn(1));
            return new Request()
                    .withBackground(editor)
                    .withComponent(searchEditor)
                    .withEnd(Terminal.identifier)
                    .run()
                    .flatMap(name -> commands.find(x -> x.command().equals(name))
                            .valueE(() -> "Could not find command " + name)
                            .apply(((Proton) editor.ast)))
                    .map(Editor::new)
                    .runUnsafe();
        } else {
            return editor;
        }
    }
}
