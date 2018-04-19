package io.github.aedans.proton.system.proton;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ui.AstRenderer;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class ProtonRenderer implements AstRenderer<Proton> {
    @Override
    public Stream<Seq<TextCharacter>> render(Proton proton, TerminalSize size) {
        int width = proton.getEditorWidth(size);
        TerminalSize realSize = size.withColumns(width);
        Seq<Stream<Seq<TextCharacter>>> renders = proton.editors
                .map(x -> {
                    @SuppressWarnings("unchecked")
                    Stream<Seq<TextCharacter>> render = x.renderer.render(x.ast, realSize);
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
        return new TerminalPosition(proton.focus * proton.getEditorWidth(size), 0);
    }

    @Override
    public Key key() {
        return Proton.key;
    }
}
