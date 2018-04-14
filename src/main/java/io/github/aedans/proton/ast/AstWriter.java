package io.github.aedans.proton.ast;

import io.github.aedans.proton.util.Unique;
import org.pf4j.ExtensionPoint;

import java.io.Writer;

public interface AstWriter extends ExtensionPoint, Unique {
    void write(Ast ast, Writer output);
}
