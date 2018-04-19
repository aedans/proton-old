package io.github.aedans.proton.system.directory;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ui.AstRenderer;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class DirectoryRenderer implements AstRenderer<Directory> {
    @Override
    public Key key() {
        return Directory.key;
    }

    @Override
    public Stream<Seq<TextCharacter>> render(Directory directory, TerminalSize size) {
        Stream<String> names = directory.getNames().toStream();
        Stream<String> directories = names.filter(x -> directory.get(x).some() instanceof Directory);
        Stream<String> files = names.filter(x -> !(directory.get(x).some() instanceof Directory));
        return directories.map(x -> "> " + x).map(TextString::fromString)
                .append(files.map(TextString::fromString));
    }
}
