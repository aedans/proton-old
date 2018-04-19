package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.util.Key;

import java.util.function.UnaryOperator;

public final class Text implements Ast {
    public static final Key key = Key.unique("text");

    public final Stream<Seq<TextCharacter>> text;
    public final TerminalPosition cursor;
    public final TerminalSize scroll;

    public Text() {
        this(Stream.single(Seq.empty()));
    }

    public Text(Stream<Seq<TextCharacter>> text) {
        this(text, TerminalPosition.TOP_LEFT_CORNER, TerminalSize.ZERO);
    }

    public Text(Stream<Seq<TextCharacter>> text, TerminalPosition cursor, TerminalSize scroll) {
        this.text = text;
        this.cursor = cursor;
        this.scroll = scroll;
    }

    public Seq<TextCharacter> getLine(int i) {
        return text.index(i);
    }

    public int lines() {
        return text.length();
    }

    public int getRow() {
        return scroll.getRows() + cursor.getRow();
    }

    public int getColumn() {
        return scroll.getColumns() + cursor.getColumn();
    }

    public Text normalize(TerminalSize size) {
        int rows = size.getRows() - 1;
        int columns = size.getColumns() - 1;

        if (cursor.getRow() < 0) {
            int distance = cursor.getRow();
            return this
                    .mapScroll(scroll -> scroll.withRelativeRows(Math.max(distance, -scroll.getRows())))
                    .mapCursor(cursor -> cursor.withRow(0))
                    .normalize(size);
        } else if (cursor.getRow() > rows) {
            int distance = cursor.getRow() - rows;
            return this
                    .mapScroll(scroll -> scroll.withRelativeRows(distance))
                    .mapCursor(cursor -> cursor.withRow(rows))
                    .normalize(size);
        } else if (cursor.getColumn() < 0) {
            int distance = cursor.getColumn();
            return this
                    .mapScroll(scroll -> scroll.withRelativeColumns(Math.max(distance, -scroll.getColumns())))
                    .mapCursor(cursor -> cursor.withColumn(0))
                    .normalize(size);
        } else if (cursor.getColumn() > columns) {
            int distance = cursor.getColumn() - columns;
            return this
                    .mapScroll(scroll -> scroll.withRelativeColumns(distance))
                    .mapCursor(cursor -> cursor.withColumn(columns))
                    .normalize(size);
        } else if (getRow() < 0) {
            int distance = getRow();
            return this
                    .mapCursor(cursor -> cursor.withRelativeRow(distance))
                    .normalize(size);
        } else if (getRow() > text.length() - 1) {
            int distance = getRow() - (text.length() - 1);
            return this
                    .mapCursor(cursor -> cursor.withRelativeRow(-distance))
                    .normalize(size);
        } else if (getColumn() < 0) {
            int distance = getColumn();
            return this
                    .mapCursor(cursor -> cursor.withRelativeColumn(distance))
                    .normalize(size);
        } else if (getColumn() > text.index(getRow()).length()) {
            int distance = getColumn() - text.index(getRow()).length();
            return this
                    .mapCursor(cursor -> cursor.withRelativeColumn(-distance))
                    .normalize(size);
        } else {
            return this;
        }
    }

    public Text mapLine(int i, UnaryOperator<Seq<TextCharacter>> fn) {
        return mapText(text -> text
                .take(i)
                .snoc(fn.apply(text.index(i)))
                .append(text.drop(i + 1)));
    }

    public Text mapText(UnaryOperator<Stream<Seq<TextCharacter>>> fn) {
        return new Text(fn.apply(text), cursor, scroll);
    }

    public Text mapCursor(UnaryOperator<TerminalPosition> fn) {
        return new Text(text, fn.apply(cursor), scroll);
    }

    public Text mapScroll(UnaryOperator<TerminalSize> fn) {
        return new Text(text, cursor, fn.apply(scroll));
    }

    public Text withText(Stream<Seq<TextCharacter>> text) {
        return mapText(x -> text);
    }

    public Text withCursor(TerminalPosition cursor) {
        return mapCursor(x -> cursor);
    }

    public Text withScroll(TerminalSize scroll) {
        return mapScroll(x -> scroll);
    }

    @Override
    public Key type() {
        return key;
    }
}
