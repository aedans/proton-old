package io.github.aedans.proton.system.proton;

import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.system.directory.Directory;
import io.github.aedans.proton.system.search.Search;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.Request;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.IO;
import org.pf4j.Extension;

@Extension
public final class Open implements Command {
    @Override
    public String command() {
        return "open";
    }

    @Override
    public IO<Proton> apply(Proton proton) {
        Search search = Search.builder()
                .searchSpace(getAll(proton.directory(), false).map(TextString::fromString))
                .build();
        return Request.builder()
                .background(Editor.builder().ast(proton).build())
                .editor(Editor.builder().ast(search).build())
                .end(Terminal.line)
                .build()
                .run()
                .map(string -> {
                    Seq<String> path = Seq.arraySeq(string.split("/"));
                    Ast ast = path.<Ast>foldLeft(
                            (dir, name) -> ((Directory) dir).get(name).valueE(() -> "Could not find path " + string),
                            proton.directory()
                    );
                    Editor editor = Editor.builder().ast(ast).build();
                    return proton
                            .mapEditors(editors -> editors.insert(proton.selected() + 1, editor))
                            .withSelected(proton.editors().length());
                });
    }

    @SuppressWarnings("Convert2MethodRef")
    private Stream<String> getAll(Directory directory, boolean relative) {
        Stream<String> local = directory.names().toStream();

        Stream<String> strings = local.<Stream<String>>map(x -> {
            Ast ast = directory.get(x).some();
            return ast instanceof Directory
                    ? getAll(((Directory) ast), true)
                    : Stream.nil();
        }).foldLeft((a, b) -> a.append(b), Stream.nil());

        if (relative) {
            return local.append(strings).map(s -> directory.file().getName() + "/" + s);
        } else {
            return local.append(strings);
        }
    }
}
