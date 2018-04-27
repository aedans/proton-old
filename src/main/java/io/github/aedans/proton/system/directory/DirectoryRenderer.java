package io.github.aedans.proton.system.directory;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import fj.data.Stream;
import io.github.aedans.proton.ui.AstRenderer;
import io.github.aedans.proton.ui.AstRendererResult;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class DirectoryRenderer implements AstRenderer<Directory> {
    @Override
    public AstRendererResult render(Directory directory, TerminalSize size) {
        Stream<String> names = directory.names().toStream();
        Stream<String> directories = names.filter(x -> directory.get(x).some() instanceof Directory);
        Stream<String> files = names.filter(x -> !(directory.get(x).some() instanceof Directory));
        return AstRendererResult.of(
                directories.map(x -> "> " + x).map(TextString::fromString)
                        .append(files.map(TextString::fromString)),
                TerminalPosition.TOP_LEFT_CORNER
        );
    }

    @Override
    public Key key() {
        return Directory.key;
    }
}
