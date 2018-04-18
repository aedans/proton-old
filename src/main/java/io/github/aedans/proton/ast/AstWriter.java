package io.github.aedans.proton.ast;

import fj.data.Stream;
import io.github.aedans.proton.util.Unique;
import org.pf4j.ExtensionPoint;

public interface AstWriter<A extends Ast> extends ExtensionPoint, Unique {
    Stream<String> write(A ast);
}
