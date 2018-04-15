package io.github.aedans.proton.system.directory;

import com.googlecode.lanterna.TextCharacter;
import fj.P2;
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
    public Stream<Seq<TextCharacter>> render(Ast ast) {
        Directory directory = (Directory) ast;
        P2<Stream<String>, Stream<String>> names = directory.getNames().toStream().split(x -> directory.get(x).some() instanceof Directory);
        return names._2().map(x -> "> " + x).map(TextString::fromString)
                .append(names._1().map(TextString::fromString));
    }
}
