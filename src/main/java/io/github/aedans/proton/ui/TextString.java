package io.github.aedans.proton.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import fj.Unit;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.util.IO;

import java.util.Iterator;

public final class TextString {
    private TextString() {
    }

    public static IO<Unit> render(Stream<Seq<TextCharacter>> value, TerminalPosition offset) {
        return IO.run(() -> {
            value.foldLeft((linePosition, line) -> {
                TextString.render(line, linePosition).runUnsafe();
                return linePosition.withRelativeRow(1);
            }, offset);
        });
    }

    public static IO<Unit> render(Seq<TextCharacter> value, TerminalPosition offset) {
        return IO.run(() -> {
            value.foldLeft((position, character) -> {
                Terminal.write(character, position).runUnsafe();
                return position.withRelativeColumn(1);
            }, offset);
        });
    }

    public static Seq<TextCharacter> fromString(String string) {
        return string == null ? fromString("null") : Seq.iteratorSeq(new Iterator<TextCharacter>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < string.length();
            }

            @Override
            public TextCharacter next() {
                return new TextCharacter(string.charAt(i++));
            }
        });
    }

    public static String toString(Stream<Seq<TextCharacter>> value) {
        Stream<String> map = value.map(TextString::toString);
        return map.isEmpty() ? "" : map.foldLeft1((a, b) -> a + '\n' + b);
    }

    public static String toString(Seq<TextCharacter> value) {
        return value.map(TextCharacter::getCharacter).foldLeft((a, b) -> a + b, "");
    }
}
