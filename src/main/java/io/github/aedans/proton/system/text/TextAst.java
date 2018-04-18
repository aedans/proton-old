package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.util.Key;

import java.util.function.UnaryOperator;

public final class TextAst implements Ast {
    public static final Key key = Key.unique("text");

    public final Seq<Seq<TextCharacter>> text;

    public TextAst() {
        this(Seq.single(Seq.empty()));
    }

    public TextAst(Seq<Seq<TextCharacter>> text) {
        this.text = text;
    }

    public Seq<TextCharacter> getLine(int i) {
        return text.index(i);
    }

    public int lines() {
        return text.length();
    }

    public TextAst mapLine(int i, UnaryOperator<Seq<TextCharacter>> fn) {
        return mapText(text -> text.update(i, fn.apply(text.index(i))));
    }

    public TextAst mapText(UnaryOperator<Seq<Seq<TextCharacter>>> fn) {
        return new TextAst(fn.apply(text));
    }

    @Override
    public Key type() {
        return key;
    }
}
