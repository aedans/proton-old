package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.util.AbstractImmutable;
import io.github.aedans.proton.util.Key;
import org.immutables.value.Value;

import java.util.function.UnaryOperator;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractText implements Ast {
    public static final Key key = Key.of(AbstractText.class);

    @Value.Default
    public Stream<Seq<TextCharacter>> text() {
        return Stream.single(Seq.empty());
    }

    @Value.Default
    public TerminalPosition cursor() {
        return TerminalPosition.TOP_LEFT_CORNER;
    }

    @Value.Default
    public TerminalSize scroll() {
        return TerminalSize.ZERO;
    }

    @Value.Default
    public TerminalSize size() {
        return Terminal.size().runUnsafe();
    }

    public Seq<TextCharacter> line(int i) {
        return text().index(i);
    }

    public int lines() {
        return text().length();
    }

    public int row() {
        return scroll().getRows() + cursor().getRow();
    }

    public int column() {
        return scroll().getColumns() + cursor().getColumn();
    }

    @Value.Check
    public AbstractText normalize() {
        int rows = size().getRows() - 1;
        int columns = size().getColumns() - 1;

        if (cursor().getRow() < 0) {
            int distance = cursor().getRow();
            return this
                    .mapScroll(scroll -> scroll.withRelativeRows(Math.max(distance, -scroll.getRows())))
                    .mapCursor(cursor -> cursor.withRow(0));
        } else if (cursor().getRow() > rows) {
            int distance = cursor().getRow() - rows;
            return this
                    .mapScroll(scroll -> scroll.withRelativeRows(distance))
                    .mapCursor(cursor -> cursor.withRow(rows));
        } else if (cursor().getColumn() < 0) {
            int distance = cursor().getColumn();
            return this
                    .mapScroll(scroll -> scroll.withRelativeColumns(Math.max(distance, -scroll.getColumns())))
                    .mapCursor(cursor -> cursor.withColumn(0));
        } else if (cursor().getColumn() > columns) {
            int distance = cursor().getColumn() - columns;
            return this
                    .mapScroll(scroll -> scroll.withRelativeColumns(distance))
                    .mapCursor(cursor -> cursor.withColumn(columns));
        } else if (row() < 0) {
            int distance = row();
            return mapCursor(cursor -> cursor.withRelativeRow(distance));
        } else if (row() > text().length() - 1) {
            int distance = row() - (text().length() - 1);
            return mapCursor(cursor -> cursor.withRelativeRow(-distance));
        } else if (column() < 0) {
            int distance = column();
            return mapCursor(cursor -> cursor.withRelativeColumn(distance));
        } else if (column() > text().index(row()).length()) {
            int distance = column() - text().index(row()).length();
            return mapCursor(cursor -> cursor.withRelativeColumn(-distance));
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
        return Text.copyOf(this).withText(fn.apply(text()));
    }

    public Text mapCursor(UnaryOperator<TerminalPosition> fn) {
        return Text.copyOf(this).withCursor(fn.apply(cursor()));
    }

    public Text mapScroll(UnaryOperator<TerminalSize> fn) {
        return Text.copyOf(this).withScroll(fn.apply(scroll()));
    }

    public Text mapSize(UnaryOperator<TerminalSize> fn) {
        return Text.copyOf(this).withSize(fn.apply(size()));
    }

    @Override
    public Key type() {
        return key;
    }
}
