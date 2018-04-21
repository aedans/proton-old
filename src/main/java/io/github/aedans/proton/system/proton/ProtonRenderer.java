package io.github.aedans.proton.system.proton;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Option;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ui.AstRenderer;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class ProtonRenderer implements AstRenderer<Proton> {
    @Override
    public Stream<Seq<TextCharacter>> render(Proton proton, TerminalSize size) {
        int width = proton.editorWidth(size);
        TerminalSize realSize = size.withColumns(width);
        Seq<Stream<Seq<TextCharacter>>> renders = proton.editors()
                .map(x -> {
                    Stream<Seq<TextCharacter>> render = x.renderer().render(x.ast(), realSize);
                    while (render.length() < realSize.getRows())
                        render = render.snoc(Seq.empty());
                    return render;
                })
                .map(x -> x.map(line -> {
                    while (line.length() < realSize.getColumns())
                        line = line.snoc(TextCharacter.DEFAULT_CHARACTER);
                    return line.take(width);
                }));
        return renders.foldLeft(
                (text, render) -> text.zip(render).map(line -> line._1().append(line._2())),
                Stream.range(0, size.getRows()).map(x -> Seq.empty())
        );
    }

    @Override
    public TerminalPosition cursor(Proton proton, TerminalSize size) {
        Option<Editor> focusedEditor = proton.focusedEditor();
        if (focusedEditor.isSome()) {
            TerminalPosition cursor = focusedEditor.some().renderer().cursor(focusedEditor.some().ast(), size);
            int offset = proton.editorWidth(size) * proton.focusedEditorIndex().some();
            return cursor.withRelativeColumn(offset);
        } else {
            return new TerminalPosition(proton.selected() * proton.editorWidth(size), 0);
        }
    }

    @Override
    public Key key() {
        return Proton.key;
    }
}
