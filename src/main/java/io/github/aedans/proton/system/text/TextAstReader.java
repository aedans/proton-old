package io.github.aedans.proton.system.text;

import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.ast.AstReader;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class TextAstReader implements AstReader {
    @Override
    public Key key() {
        return TextAst.key;
    }

    @Override
    public Ast read(Stream<String> input) {
        return new TextAst(Seq.iterableSeq(input.map(TextString::fromString)));
    }
}
