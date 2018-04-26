package io.github.aedans.proton.util.pretty;

import com.googlecode.lanterna.TextCharacter;
import fj.P2;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.util.AbstractImmutable;
import org.immutables.value.Value;

import java.util.function.UnaryOperator;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractPrettyFormatterResult {
    @Value.Parameter
    public abstract int space();

    @Value.Parameter
    public abstract int position();

    @Value.Parameter
    public abstract UnaryOperator<P2<Stream<Seq<TextCharacter>>, Seq<TextCharacter>>> result();
}
