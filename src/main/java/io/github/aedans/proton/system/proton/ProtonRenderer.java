package io.github.aedans.proton.system.proton;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Option;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.AstRenderer;
import io.github.aedans.proton.ui.AstRendererResult;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

import java.util.Optional;

@Extension
public final class ProtonRenderer implements AstRenderer<Proton> {
    @SuppressWarnings("unchecked")
    @Override
    public AstRendererResult render(Proton proton, TerminalSize size) {
        int width = proton.editorWidth(size);
        TerminalSize realSize = size.withColumns(width);
        Seq<AstRendererResult> results = proton.editors().map(x -> x.renderer().render(x.ast(), realSize));
        Seq<Stream<Seq<TextCharacter>>> texts = results
                .map(AstRendererResult::text)
                .map(text -> {
                    while (text.length() < realSize.getRows())
                        text = text.snoc(Seq.empty());
                    return text;
                })
                .map(text -> text.map(line -> {
                    while (line.length() < realSize.getColumns())
                        line = line.snoc(TextCharacter.DEFAULT_CHARACTER);
                    return line.take(width);
                }));

        Option<Editor> focusedEditor = proton.focusedEditor();
        Optional<TerminalPosition> cursor;
        if (focusedEditor.isSome()) {
            int index = proton.focusedEditorIndex().some();
            Optional<TerminalPosition> cursor1 = results.index(index).cursor();
            int offset = proton.editorWidth(size) * index;
            cursor = cursor1.map(x -> x.withRelativeColumn(offset));
        } else {
            cursor = Optional.of(new TerminalPosition(proton.selected() * proton.editorWidth(size), 0));
        }

        Stream<Seq<TextCharacter>> text = texts.foldLeft(
                (x, render) -> x.zip(render).map(line -> line._1().append(line._2())),
                Stream.range(0, size.getRows()).map(x -> Seq.empty())
        );

        return AstRendererResult.of(text, cursor);
    }

    @Override
    public Key key() {
        return Proton.key;
    }
}
