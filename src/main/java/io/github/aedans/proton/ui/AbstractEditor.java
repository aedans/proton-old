package io.github.aedans.proton.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import fj.Unit;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.pfj.IO;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.util.AbstractImmutable;
import org.immutables.value.Value;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractEditor<A extends Ast> implements Renderable {
    @Value.Parameter
    public abstract A ast();

    @Value.Default
    public AstRenderer<A> renderer() {
        return AstRenderer.forKey(ast().type());
    }

    @Value.Default
    public KeyListener<A> listener() {
        return KeyListener.forKey(ast().type());
    }

    @Value.Default
    public TerminalSize size() {
        return Terminal.size().runUnsafe();
    }

    public Editor<A> accept(KeyStroke keyStroke) {
        return listener().apply(Editor.copyOf(this), keyStroke);
    }

    @Override
    public IO<Unit> render(TerminalPosition offset) {
        Stream<Seq<TextCharacter>> text = renderer().render(ast(), size());

        TerminalSize realSize = new TerminalSize(
                Math.min(text.foldLeft((max, line) -> Math.max(max, line.length()), 0), size().getColumns()),
                Math.min(text.length(), size().getRows())
        );

        return IO.empty
                .flatMap(() -> Terminal.clear(offset, realSize))
                .flatMap(() -> TextString.render(text, offset))
                .flatMap(() -> Terminal.setCursor(renderer().cursor(ast(), size())));
    }

    public String text() {
        return renderer().entry(ast());
    }

    public <B extends Ast> Editor<B> map(Function<A, B> fn) {
        return Editor.of(fn.apply(ast())).withSize(size());
    }

    public Editor<A> mapAst(UnaryOperator<A> fn) {
        return Editor.copyOf(this).withAst(fn.apply(ast()));
    }

    public Editor<A> mapRenderer(UnaryOperator<AstRenderer<A>> fn) {
        return Editor.copyOf(this).withRenderer(fn.apply(renderer()));
    }

    public Editor<A> mapListener(UnaryOperator<KeyListener<A>> fn) {
        return Editor.copyOf(this).withListener(fn.apply(listener()));
    }

    public Editor<A> mapSize(UnaryOperator<TerminalSize> fn) {
        return Editor.copyOf(this).withSize(fn.apply(size()));
    }
}
