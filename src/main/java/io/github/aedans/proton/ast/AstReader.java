package io.github.aedans.proton.ast;

import fj.data.Stream;
import io.github.aedans.proton.util.Unique;
import org.pf4j.ExtensionPoint;

public interface AstReader extends ExtensionPoint, Unique {
    Ast read(Stream<String> input);
}
