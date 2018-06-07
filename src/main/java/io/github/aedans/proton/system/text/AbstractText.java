package io.github.aedans.proton.system.text;

import org.immutables.value.Value;

import io.github.aedans.proton.util.AbstractImmutable;
import io.github.aedans.proton.util.Ast;
import io.vavr.collection.Stream;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractText implements Ast {
    @Value.Parameter
    public abstract Stream<String> lines();    
}