package io.github.aedans.proton.ast;

import fj.data.Stream;
import io.github.aedans.proton.util.Unique;
import org.pf4j.ExtensionPoint;

public interface AstWriter extends ExtensionPoint, Unique {
    Stream<String> write(Ast ast);
}
