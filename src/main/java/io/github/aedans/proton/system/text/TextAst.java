package io.github.aedans.proton.system.text;

import fj.data.Seq;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.util.Key;

public final class TextAst implements Ast {
    public static final Key key = Key.unique("text");

    public final Seq<String> text;

    public TextAst(Seq<String> text) {
        this.text = text;
    }

    @Override
    public Key type() {
        return key;
    }
}
