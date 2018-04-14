package io.github.aedans.proton.system.text;

import fj.data.Seq;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.ast.AstReader;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

import java.io.BufferedReader;
import java.io.Reader;

@Extension
public final class TextAstReader implements AstReader {
    @Override
    public Key key() {
        return TextAst.key;
    }

    @Override
    public Ast read(Reader input) {
        return new TextAst(Seq.iteratorSeq(new BufferedReader(input).lines().iterator()));
    }
}
