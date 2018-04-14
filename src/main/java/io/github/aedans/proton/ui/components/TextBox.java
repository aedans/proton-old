package io.github.aedans.proton.ui.components;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import fj.data.List;
import fj.data.Seq;
import io.github.aedans.proton.logic.Plugins;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.ui.TextComponent;
import io.github.aedans.proton.ui.TextString;
import org.pf4j.ExtensionPoint;

import java.util.function.UnaryOperator;

public final class TextBox implements TextComponent {
    public static final List<KeyListener> keyListeners = Plugins.all(KeyListener.class);

    public static final Seq<Seq<TextCharacter>> empty = Seq.single(Seq.empty());

    public final TerminalPosition cursor;
    public final Seq<Seq<TextCharacter>> text;
    public final TerminalSize scroll;

    public TextBox() {
        this(TerminalPosition.TOP_LEFT_CORNER
                        .withRelativeRow(empty.length() - 1)
                        .withRelativeColumn(empty.last().length()), empty, TerminalSize.ZERO);
    }

    public TextBox(
            TerminalPosition cursor,
            Seq<Seq<TextCharacter>> text,
            TerminalSize scroll
    ) {
        this.cursor = cursor;
        this.text = text;
        this.scroll = scroll;
    }

    public int getRow() {
        return scroll.getRows() + cursor.getRow();
    }

    public int getColumn() {
        return scroll.getColumns() + cursor.getColumn();
    }

    public Seq<TextCharacter> getLine(int i) {
        return text.index(i);
    }

    public int lines() {
        return text.length();
    }

    public TextBox fix(TerminalSize size) {
        int rows = size.getRows() - 1;
        int columns = size.getColumns() - 1;

        if (cursor.getRow() < 0) {
            int distance = cursor.getRow();
            return this
                    .mapScroll(scroll -> scroll.withRelativeRows(Math.max(distance, -scroll.getRows())))
                    .mapCursor(cursor -> cursor.withRow(0))
                    .fix(size);
        } else if (cursor.getRow() > rows) {
            int distance = cursor.getRow() - rows;
            return this
                    .mapScroll(scroll -> scroll.withRelativeRows(distance))
                    .mapCursor(cursor -> cursor.withRow(rows))
                    .fix(size);
        } else if (cursor.getColumn() < 0) {
            int distance = cursor.getColumn();
            return this
                    .mapScroll(scroll -> scroll.withRelativeColumns(Math.max(distance, -scroll.getColumns())))
                    .mapCursor(cursor -> cursor.withColumn(0))
                    .fix(size);
        } else if (cursor.getColumn() > columns) {
            int distance = cursor.getColumn() - columns;
            return this
                    .mapScroll(scroll -> scroll.withRelativeColumns(distance))
                    .mapCursor(cursor -> cursor.withColumn(columns))
                    .fix(size);
        } else if (getRow() < 0) {
            int distance = getRow();
            return this
                    .mapCursor(cursor -> cursor.withRelativeRow(distance))
                    .fix(size);
        } else if (getRow() > text.length() - 1) {
            int distance = getRow() - (text.length() - 1);
            return this
                    .mapCursor(cursor -> cursor.withRelativeRow(-distance))
                    .fix(size);
        } else if (getColumn() < 0) {
            int distance = getColumn();
            return this
                    .mapCursor(cursor -> cursor.withRelativeColumn(distance))
                    .fix(size);
        } else if (getColumn() > getLine(getRow()).length()) {
            int distance = getColumn() - getLine(getRow()).length();
            return this
                    .mapCursor(cursor -> cursor.withRelativeColumn(-distance))
                    .fix(size);
        } else {
            return this;
        }
    }

    public TextBox mapLine(int i, UnaryOperator<Seq<TextCharacter>> fn) {
        return mapText(text -> text.update(i, fn.apply(text.index(i))));
    }

    public TextBox mapCursor(UnaryOperator<TerminalPosition> fn) {
        return new TextBox(fn.apply(cursor), text, scroll);
    }

    public TextBox mapText(UnaryOperator<Seq<Seq<TextCharacter>>> fn) {
        return new TextBox(cursor, fn.apply(text), scroll);
    }

    public TextBox mapScroll(UnaryOperator<TerminalSize> fn) {
        return new TextBox(cursor, text, fn.apply(scroll));
    }

    public TextBox withCursor(TerminalPosition cursor) {
        return mapCursor(x -> cursor);
    }

    public TextBox withText(Seq<Seq<TextCharacter>> text) {
        return mapText(x -> text);
    }

    public TextBox withScroll(TerminalSize scroll) {
        return mapScroll(x -> scroll);
    }

    @Override
    public TextBox accept(KeyStroke keyStroke) {
        return keyListeners
                .foldLeft((textBox, keyListener) -> keyListener.apply(textBox, keyStroke), this)
                .fix(Terminal.size());
    }

    @Override
    public void render(TerminalPosition offset, TerminalSize size) {
        TextBox textBox = fix(size);

        TerminalSize realSize = new TerminalSize(
                Math.min(textBox.text.foldLeft((max, text) -> Math.max(max, text.length()), 0), size.getColumns()),
                Math.min(textBox.text.length(), size.getRows())
        );

        Terminal.clear(offset, realSize);

        TextString.render(textBox.text
                .drop(textBox.scroll.getRows())
                .map(x -> x.drop(textBox.scroll.getColumns()))
                .toStream(), offset);

        Terminal.setCursor(textBox.cursor);
    }

    @Override
    public String text() {
        return TextString.toString(text.toStream());
    }

    public interface KeyListener extends ExtensionPoint {
        TextBox apply(TextBox textBox, KeyStroke keyStroke);
    }
}
