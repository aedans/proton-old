package io.github.aedans.proton.ui;

import com.googlecode.lanterna.input.KeyStroke;
import fj.P;
import fj.Unit;
import fj.control.Trampoline;
import io.github.aedans.proton.util.AbstractImmutable;
import io.github.aedans.proton.util.IO;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractRequest {
    @Value.Parameter
    public abstract Optional<Renderable> background();

    @Value.Parameter
    public abstract Editor editor();

    @Value.Parameter
    public abstract Predicate<KeyStroke> end();

    public IO<String> run() {
        return runT(editor()).map(Trampoline::run);
    }

    private IO<Trampoline<String>> runT(Editor editor) {
        return IO.empty
                .flatMap(x -> background().isPresent() ? background().get().render() : IO.pure(x))
                .flatMap((Supplier<IO<Unit>>) editor::render)
                .flatMap(Terminal::refresh)
                .flatMap(Terminal::read)
                .flatMap(in -> {
                    if (end().test(in)) {
                        return Terminal.resetCursor().map(x -> Trampoline.pure(editor.text()));
                    } else {
                        return IO.pure(Trampoline.suspend(P.lazy(() -> runT(editor.accept(in)).runUnsafe())));
                    }
                });
    }
}
