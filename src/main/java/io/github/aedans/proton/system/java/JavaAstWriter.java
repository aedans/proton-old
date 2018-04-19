package io.github.aedans.proton.system.java;

import fj.data.Collectors;
import fj.data.Stream;
import io.github.aedans.proton.ast.AstWriter;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

import java.io.BufferedReader;
import java.io.StringReader;

@Extension
public final class JavaAstWriter implements AstWriter<JavaAst> {
    @Override
    public Stream<String> write(JavaAst ast) {
        String string = ast.getCompilationUnit().toString();
        return new BufferedReader(new StringReader(string)).lines()
                .collect(Collectors.toStream());
    }

    @Override
    public Key key() {
        return JavaAst.key;
    }
}
