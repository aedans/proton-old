package io.github.aedans.proton.system.java;

import io.github.aedans.proton.ast.FileAssociation;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class JavaFileAssociation implements FileAssociation {
    @Override
    public String extension() {
        return "java";
    }

    @Override
    public Key astKey() {
        return JavaAst.key;
    }
}
