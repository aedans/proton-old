package io.github.aedans.proton.system.text;

import fj.data.Stream;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.ast.AstWriter;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class TextAstWriter implements AstWriter {
    @Override
    public Key key() {
        return TextAst.key;
    }

    @Override
    public Stream<String> write(Ast ast) {
        return ((TextAst) ast).text.map(TextString::toString).toStream();
    }
}
