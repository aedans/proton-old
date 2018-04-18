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
    public final TerminalPosition cursor;
    public final TerminalSize scroll;
    public final TerminalSize size;
    public final Seq<String> path;

    public Editor(A ast) {
        this(
                ast,
                AstRenderer.forKey(ast.type()),
                KeyListener.forKey(ast.type()),
                TerminalPosition.TOP_LEFT_CORNER,
                TerminalSize.ZERO,
                Terminal.size().runUnsafe(),
                Seq.empty()
        );
    }

    public Editor(
            A ast,
            AstRenderer<A> renderer,
            KeyListener<A> listener,
            TerminalPosition cursor,
            TerminalSize scroll,
            TerminalSize size,
            Seq<String> path
    ) {
        this.ast = ast;
        this.renderer = renderer;
        this.listener = listener;
        this.cursor = cursor;
        this.scroll = scroll;
        this.size = size;
        this.path = path;
    }

    private Stream<Seq<TextCharacter>> text;
    public Stream<Seq<TextCharacter>> astText() {
        if (text == null) {
            text = renderer.render(ast, size);
            return text;
        } else {
            return text;
        }
    }

    public Editor<A> accept(KeyStroke keyStroke) {
        return listener.apply(this, keyStroke).fix();
    }

    @Override
    public IO<Unit> render(TerminalPosition offset) {
        Stream<Seq<TextCharacter>> text = astText();

        TerminalSize realSize = new TerminalSize(
                Math.min(text.foldLeft((max, line) -> Math.max(max, line.length()), 0), size.getColumns()),
                Math.min(text.length(), size.getRows())
        );

        return IO.empty
                .flatMap(() -> Terminal.clear(offset, realSize))
                .flatMap(() -> Terminal.setCursor(cursor))
                .flatMap(() -> TextString.render(text, offset));
    }

    public String text() {
        return renderer.text(ast);
    }

    public int getRow() {
        return scroll.getRows() + cursor.getRow();
    }

    public Editor<A> fix() {
        int rows = size.getRows() - 1;
        int columns = size.getColumns() - 1;
        Stream<Seq<TextCharacter>> text = astText();

        if (cursor.getRow() < 0) {
            int distance = cursor.getRow();
            return this
                    .mapScroll(scroll -> scroll.withRelativeRows(Math.max(distance, -scroll.getRows())))
                    .mapCursor(cursor -> cursor.withRow(0))
                    .fix();
        } else if (cursor.getRow() > rows) {
            int distance = cursor.getRow() - rows;
            return this
                    .mapScroll(scroll -> scroll.withRelativeRows(distance))
                    .mapCursor(cursor -> cursor.withRow(rows))
                    .fix();
        } else if (cursor.getColumn() < 0) {
            int distance = cursor.getColumn();
            return this
                    .mapScroll(scroll -> scroll.withRelativeColumns(Math.max(distance, -scroll.getColumns())))
                    .mapCursor(cursor -> cursor.withColumn(0))
                    .fix();
        } else if (cursor.getColumn() > columns) {
            int distance = cursor.getColumn() - columns;
            return this
                    .mapScroll(scroll -> scroll.withRelativeColumns(distance))
                    .mapCursor(cursor -> cursor.withColumn(columns))
                    .fix();
        } else if (getRow() < 0) {
            int distance = getRow();
            return this
                    .mapCursor(cursor -> cursor.withRelativeRow(distance))
                    .fix();
        } else if (getRow() > text.length() - 1) {
            int distance = getRow() - (text.length() - 1);
            return this
                    .mapCursor(cursor -> cursor.withRelativeRow(-distance))
                    .fix();
        } else if (getColumn() < 0) {
            int distance = getColumn();
            return this
                    .mapCursor(cursor -> cursor.withRelativeColumn(distance))
                    .fix();
        } else if (getColumn() > text.index(getRow()).length()) {
            int distance = getColumn() - text.index(getRow()).length();
            return this
                    .mapCursor(cursor -> cursor.withRelativeColumn(-distance))
                    .fix();
        } else {
            return this;
        }
    }

    public int getColumn() {
        return scroll.getColumns() + cursor.getColumn();
    }

    public <B extends Ast> Editor<B> map(Function<A, B> fn) {
        return new Editor<>(fn.apply(ast))
                .withCursor(cursor)
                .withScroll(scroll)
                .withSize(size)
                .withPath(path);
    }

    public Editor<A> mapAst(UnaryOperator<A> fn) {
        return new Editor<>(fn.apply(ast), renderer, listener, cursor, scroll, size, path);
    }

    public Editor<A> mapRenderer(UnaryOperator<AstRenderer<A>> fn) {
        return new Editor<>(ast, fn.apply(renderer), listener, cursor, scroll, size, path);
    }

    public Editor<A> mapListener(UnaryOperator<KeyListener<A>> fn) {
        return new Editor<>(ast, renderer, fn.apply(listener), cursor, scroll, size, path);
    }

    public Editor<A> mapCursor(UnaryOperator<TerminalPosition> fn) {
        return new Editor<>(ast, renderer, listener, fn.apply(cursor), scroll, size, path);
    }

    public Editor<A> mapScroll(UnaryOperator<TerminalSize> fn) {
        return new Editor<>(ast, renderer, listener, cursor, fn.apply(scroll), size, path);
    }

    public Editor<A> mapSize(UnaryOperator<TerminalSize> fn) {
        return new Editor<>(ast, renderer, listener, cursor, scroll, fn.apply(size), path);
    }

    public Editor<A> mapPath(UnaryOperator<Seq<String>> fn) {
        return new Editor<>(ast, renderer, listener, cursor, scroll, size, fn.apply(path));
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

    public Editor<A> withCursor(TerminalPosition cursor) {
        return mapCursor(x -> cursor);
    }

    public Editor<A> withScroll(TerminalSize scroll) {
        return mapScroll(x -> scroll);
    }

    public Editor<A> withSize(TerminalSize size) {
        return mapSize(x -> size);
    }

    public Editor<A> withPath(Seq<String> path) {
        return mapPath(x -> path);
    }
}
