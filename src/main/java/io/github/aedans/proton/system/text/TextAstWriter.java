package io.github.aedans.proton.system.text;

import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.ast.AstWriter;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

@Extension
public final class TextAstWriter implements AstWriter {
    @Override
    public Key key() {
        return TextAst.key;
    }

    @Override
    public Stream<String> write(Ast ast) {
        return ((TextAst) ast).text.toStream();
    }
}
