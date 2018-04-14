package io.github.aedans.proton.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;

import java.util.Iterator;

public final class TextString {
    private TextString() {
    }

    public static void render(Stream<Seq<TextCharacter>> value, TerminalPosition offset) {
        value.foldLeft((linePosition, line) -> {
            TextString.render(line, linePosition);
            return linePosition.withRelativeRow(1);
        }, offset);
    }

    public static void render(Seq<TextCharacter> value, TerminalPosition offset) {
        value.foldLeft((position, character) -> {
            Terminal.write(character, position);
            return position.withRelativeColumn(1);
        }, offset);
    }

    public static Seq<TextCharacter> fromString(String string) {
        return Seq.iteratorSeq(new Iterator<TextCharacter>() {
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
        return value.map(TextString::toString).foldLeft1((a, b) -> a + '\n' + b);
    }

    public static String toString(Seq<TextCharacter> value) {
        return value.map(TextCharacter::getCharacter).foldLeft(StringBuilder::append, new StringBuilder()).toString();
    }
}
