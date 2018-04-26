package io.github.aedans.proton.util.pretty;

import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.util.AbstractImmutable;
import org.immutables.value.Value;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractPrettyFormatterResultState {
    @Value.Parameter
    public abstract Stream<Seq<TextCharacter>> before();

    @Value.Parameter
    public abstract Seq<TextCharacter> line();

    public Stream<Seq<TextCharacter>> all() {
        return before().cons(line());
    }
}
