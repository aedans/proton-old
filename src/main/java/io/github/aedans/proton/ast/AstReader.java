package io.github.aedans.proton.ast;

import io.github.aedans.proton.util.Unique;
import org.pf4j.ExtensionPoint;

import java.io.Reader;

public interface AstReader extends ExtensionPoint, Unique {
    Ast read(Reader input);
}
