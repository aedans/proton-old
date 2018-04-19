package io.github.aedans.proton.system.java;

import com.github.javaparser.ast.CompilationUnit;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.util.Key;

public final class JavaAst implements Ast {
    public static final Key key = Key.unique("java");

    private final CompilationUnit compilationUnit;

    public JavaAst(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit.clone();
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit.clone();
    }

    @Override
    public Key type() {
        return key;
    }
}
