package io.github.aedans.proton.system.directory;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.ast.Directory;
import io.github.aedans.proton.ui.AstRenderer;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class DirectoryAstRenderer implements AstRenderer {
    @Override
    public Key key() {
        return Directory.type;
    }

    @Override
    public Stream<Seq<TextCharacter>> render(Ast ast, TerminalSize size) {
        Directory directory = (Directory) ast;
        Stream<String> names = directory.getNames().toStream();
        Stream<String> directories = names.filter(x -> directory.get(x).some() instanceof Directory);
        Stream<String> files = names.filter(x -> !(directory.get(x).some() instanceof Directory));
        return directories.map(x -> "> " + x).map(TextString::fromString)
                .append(files.map(TextString::fromString));
    }
}
