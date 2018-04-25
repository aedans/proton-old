package io.github.aedans.proton.system.proton;

import io.github.aedans.pfj.IO;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.ast.FileAssociation;
import io.github.aedans.proton.system.text.Text;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.Request;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.util.FileUtils;
import io.github.aedans.proton.util.Key;
import io.github.aedans.proton.util.Plugins;
import io.github.aedans.proton.util.Unique;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;

@Extension
public final class New implements Command {
    @Override
    public String command() {
        return "new";
    }

    @Override
    public Key type() {
        return Proton.none;
    }

    @Override
    public IO<Proton> apply(Proton proton) {
        return Request.builder()
                .background(Editor.builder().ast(proton).build())
                .editor(Editor.builder().ast(Text.builder().build()).build())
                .end(Terminal.line)
                .build()
                .run()
                .map(name -> {
                    Key key = FileAssociation.from(FileUtils.extension(name));
                    Empty empty = Plugins.forKey(Empty.class, key)
                            .valueE("Could not create empty ast for " + key);
                    Ast ast = empty.create();
                    Editor editor = Editor.of(ast);
                    return proton
                            .mapDirectory(directory -> directory.put(name, () -> ast))
                            .mapEditors(editors -> editors.insert(proton.selected() + 1, editor))
                            .withSelected(proton.editors().length());
                });
    }

    public interface Empty<T extends Ast> extends ExtensionPoint, Unique {
        T create();
    }
}
