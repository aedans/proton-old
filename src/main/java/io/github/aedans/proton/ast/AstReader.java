package io.github.aedans.proton.ast;

import fj.data.Stream;
import io.github.aedans.proton.util.Unique;
import org.pf4j.ExtensionPoint;

public interface AstReader<A extends Ast> extends ExtensionPoint, Unique {
    A read(Stream<String> input);
}
