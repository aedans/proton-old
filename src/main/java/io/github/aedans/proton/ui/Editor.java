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

import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class Editor<A extends Ast> implements Renderable {
    public final A ast;
    public final AstRenderer<A> renderer;
    public final KeyListener<A> listener;
    public final TerminalSize size;
    public final Seq<String> path;

    public Editor(A ast) {
        this(
                ast,
                AstRenderer.forKey(ast.type()),
                KeyListener.forKey(ast.type()),
                Terminal.size().runUnsafe(),
                Seq.empty()
        );
    }

    public Editor(
            A ast,
            AstRenderer<A> renderer,
            KeyListener<A> listener,
            TerminalSize size,
            Seq<String> path
    ) {
        this.ast = ast;
        this.renderer = renderer;
        this.listener = listener;
        this.size = size;
        this.path = path;
    }

    public Editor<A> accept(KeyStroke keyStroke) {
        return listener.apply(this, keyStroke);
    }

    @Override
    public IO<Unit> render(TerminalPosition offset) {
        Stream<Seq<TextCharacter>> text = renderer.render(ast, size);

        TerminalSize realSize = new TerminalSize(
                Math.min(text.foldLeft((max, line) -> Math.max(max, line.length()), 0), size.getColumns()),
                Math.min(text.length(), size.getRows())
        );

        return IO.empty
                .flatMap(() -> Terminal.clear(offset, realSize))
                .flatMap(() -> TextString.render(text, offset))
                .flatMap(() -> Terminal.setCursor(renderer.cursor(ast, size)));
    }

    public String text() {
        return renderer.text(ast);
    }

    public <B extends Ast> Editor<B> map(Function<A, B> fn) {
        return new Editor<>(fn.apply(ast))
                .withSize(size)
                .withPath(path);
    }

    public Editor<A> mapAst(UnaryOperator<A> fn) {
        return new Editor<>(fn.apply(ast), renderer, listener, size, path);
    }

    public Editor<A> mapRenderer(UnaryOperator<AstRenderer<A>> fn) {
        return new Editor<>(ast, fn.apply(renderer), listener, size, path);
    }

    public Editor<A> mapListener(UnaryOperator<KeyListener<A>> fn) {
        return new Editor<>(ast, renderer, fn.apply(listener), size, path);
    }

    public Editor<A> mapSize(UnaryOperator<TerminalSize> fn) {
        return new Editor<>(ast, renderer, listener, fn.apply(size), path);
    }

    public Editor<A> mapPath(UnaryOperator<Seq<String>> fn) {
        return new Editor<>(ast, renderer, listener, size, fn.apply(path));
    }

    public Editor<A> withAst(A ast) {
        return mapAst(x -> ast);
    }

    public Editor<A> withRenderer(AstRenderer<A> renderer) {
        return mapRenderer(x -> renderer);
    }

    public Editor<A> withListener(KeyListener<A> listener) {
        return mapListener(x -> listener);
    }

    public Editor<A> withSize(TerminalSize size) {
        return mapSize(x -> size);
    }

    public Editor<A> withPath(Seq<String> path) {
        return mapPath(x -> path);
    }
}
