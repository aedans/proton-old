package io.github.aedans.proton.util.pretty;

import com.googlecode.lanterna.TerminalPosition;
import io.github.aedans.proton.util.AbstractImmutable;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.function.UnaryOperator;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractPrettyFormatterResult {
    @Value.Parameter
    public abstract int space();

    @Value.Parameter
    public abstract int position();

    @Value.Parameter
    public abstract UnaryOperator<PrettyFormatterResultState> result();

    @Value.Parameter
    public abstract Optional<TerminalPosition> cursor();
}