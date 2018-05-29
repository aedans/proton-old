package io.github.aedans.proton.lanterna;

import com.googlecode.lanterna.TextCharacter;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vavr.collection.Vector;

import java.util.Iterator;
import java.util.function.UnaryOperator;

public final class TextString implements Iterable<TextCharacter> {
    private final Vector<TextCharacter> elems;

    public TextString() {
        this(Vector.empty());
    }

    public TextString(Vector<TextCharacter> elems) {
        this.elems = elems;
    }

    public TextString append(TextCharacter elem) {
        return new TextString(this.elems.append(elem));
    }

    public TextString appendAll(Iterable<TextCharacter> elems) {
        return new TextString(this.elems.appendAll(elems));
    }

    public TextString removeAt(int i) {
        return new TextString(elems.removeAt(i));
    }

    public TextString insert(int i, TextCharacter character) {
        return new TextString(elems.insert(i, character));
    }

    public TextString take(int i) {
        return new TextString(elems.take(i));
    }

    public TextString drop(int i) {
        return new TextString(elems.drop(i));
    }

    public int length() {
        return elems.length();
    }

    public TextString map(UnaryOperator<TextCharacter> fn) {
        return new TextString(elems.map(fn));
    }

    @Override
    public Iterator<TextCharacter> iterator() {
        return elems.iterator();
    }

    public static TextString of(Iterable<TextCharacter> characters) {
        return new TextString(Vector.ofAll(characters));
    }

    public static TextString of(TextCharacter... characters) {
        return new TextString(Vector.of(characters));
    }

    public static TextString fromString(String string) {
        return string == null ? fromString("null") : new TextString(Vector.ofAll(string.toCharArray()).map(TextCharacter::new));
    }

    public static Single<String> toString(Observable<TextString> value) {
        Observable<String> strings = value.map(TextString::toString);
        return strings.reduce((a, b) -> a + "\n" + b).toSingle("");
    }

    @Override
    public String toString() {
        return elems.foldLeft("", (a, b) -> a + b.getCharacter());
    }
}