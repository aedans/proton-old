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
import io.github.aedans.proton.util.Plugins;

import java.util.function.UnaryOperator;

public final class Editor implements Renderable {
    public final Ast ast;
    public final AstRenderer renderer;
    public final KeyListener listener;
    public final TerminalPosition cursor;
    public final TerminalSize scroll;
    public final TerminalSize size;
    public final Seq<String> path;

    public Editor(Ast ast) {
        this(
                ast,
                Plugins.forKey(AstRenderer.class, ast.type()).valueE("Could not find ast renderer for " + ast.type()),
                Plugins.forKey(KeyListener.class, ast.type()).valueE("Could not find key listener for " + ast.type()),
                TerminalPosition.TOP_LEFT_CORNER,
                TerminalSize.ZERO,
                Terminal.size().runUnsafe(),
                Seq.empty()
        );
    }

    public Editor(
            Ast ast,
            AstRenderer renderer,
            KeyListener listener,
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

    public Editor accept(KeyStroke keyStroke) {
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

    public Editor fix() {
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

    public Editor mapAst(UnaryOperator<Ast> fn) {
        return new Editor(fn.apply(ast), renderer, listener, cursor, scroll, size, path);
    }

    public Editor mapRenderer(UnaryOperator<AstRenderer> fn) {
        return new Editor(ast, fn.apply(renderer), listener, cursor, scroll, size, path);
    }

    public Editor mapCursor(UnaryOperator<TerminalPosition> fn) {
        return new Editor(ast, renderer, listener, fn.apply(cursor), scroll, size, path);
    }

    public Editor mapScroll(UnaryOperator<TerminalSize> fn) {
        return new Editor(ast, renderer, listener, cursor, fn.apply(scroll), size, path);
    }

    public Editor mapSize(UnaryOperator<TerminalSize> fn) {
        return new Editor(ast, renderer, listener, cursor, scroll, fn.apply(size), path);
    }

    public Editor mapPath(UnaryOperator<Seq<String>> fn) {
        return new Editor(ast, renderer, listener, cursor, scroll, size, fn.apply(path));
    }

    public Editor withAst(Ast ast) {
        return mapAst(x -> ast);
    }

    public Editor withRenderer(AstRenderer renderer) {
        return mapRenderer(x -> renderer);
    }

    public Editor withCursor(TerminalPosition cursor) {
        return mapCursor(x -> cursor);
    }

    public Editor withScroll(TerminalSize scroll) {
        return mapScroll(x -> scroll);
    }

    public Editor withSize(TerminalSize size) {
        return mapSize(x -> size);
    }

    public Editor withPath(Seq<String> path) {
        return mapPath(x -> path);
    }
}
