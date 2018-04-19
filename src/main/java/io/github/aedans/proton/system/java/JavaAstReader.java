package io.github.aedans.proton.system.java;

import com.github.javaparser.JavaParser;
import fj.data.Stream;
import io.github.aedans.proton.ast.AstReader;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class JavaAstReader implements AstReader<JavaAst> {
    @Override
    public JavaAst read(Stream<String> input) {
        String string = input.foldLeft1((a, b) -> a + b);
        return new JavaAst(JavaParser.parse(string));
    }

    @Override
    public Key key() {
        return JavaAst.key;
    }
}
