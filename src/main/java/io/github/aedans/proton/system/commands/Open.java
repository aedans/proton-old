package io.github.aedans.proton.system.commands;

import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.pfj.IO;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.system.directory.Directory;
import io.github.aedans.proton.system.proton.Command;
import io.github.aedans.proton.system.proton.Proton;
import io.github.aedans.proton.system.search.Search;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.Request;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class Open implements Command {
    @Override
    public String command() {
        return "open";
    }

    @Override
    public Key type() {
        return Proton.none;
    }

    @Override
    public IO<Proton> apply(Proton proton) {
        Search search = new Search().withSearchSpace(getAll(proton.directory, false).map(TextString::fromString));
        return new Request()
                .withBackground(new Editor<>(proton))
                .withEditor(new Editor<>(search))
                .withEnd(Terminal.line)
                .run()
                .map(string -> {
                    Seq<String> path = Seq.arraySeq(string.split("/"));
                    Ast ast = path.<Ast>foldLeft(
                            (dir, name) -> ((Directory) dir).get(name).valueE(() -> "Could not find path " + string),
                            proton.directory
                    );
                    Editor editor = new Editor<>(ast).withPath(path);
                    return proton
                            .mapEditors(editors -> editors.insert(proton.selected + 1, editor))
                            .withSelected(proton.editors.length())
                            .withFocused(true);
                });
    }

    @SuppressWarnings({"ConstantConditions", "Convert2MethodRef"})
    private Stream<String> getAll(Directory directory, boolean relative) {
        Stream<String> local = directory.getNames().toStream();

        Stream<String> strings = local.<Stream<String>>map(x -> {
            Ast ast = directory.get(x).some();
            return ast instanceof Directory
                    ? getAll(((Directory) ast), true)
                    : Stream.nil();
        }).foldLeft((a, b) -> a.append(b), Stream.<String>nil());

        if (relative) {
            return local.append(strings).map(s -> directory.file.getName() + "/" + s);
        } else {
            return local.append(strings);
        }
    }
}
