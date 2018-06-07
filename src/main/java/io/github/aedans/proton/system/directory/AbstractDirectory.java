package io.github.aedans.proton.system.directory;

import org.immutables.value.Value;

import io.github.aedans.proton.util.AbstractImmutable;
import io.github.aedans.proton.util.Ast;
import io.vavr.collection.Map;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractDirectory implements Ast {
    @Value.Parameter
    public abstract Map<String, Ast> contents();
}