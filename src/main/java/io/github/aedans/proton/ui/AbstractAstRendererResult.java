package io.github.aedans.proton.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.util.AbstractImmutable;
import org.immutables.value.Value;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractAstRendererResult {
    @Value.Parameter
    public abstract Stream<Seq<TextCharacter>> text();

    @Value.Parameter
    public abstract TerminalPosition cursor();
}
