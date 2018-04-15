package io.github.aedans.proton.ui;

import com.googlecode.lanterna.input.KeyStroke;
import fj.P1;
import fj.control.Trampoline;
import fj.data.Option;
import io.github.aedans.pfj.IO;
import io.github.aedans.proton.ui.components.TextBox;

import java.util.function.Predicate;

public final class Request {
    private final Option<Component> background;
    private final TextComponent component;
    private final Predicate<KeyStroke> end;

    public Request() {
        this(Option.none(), new TextBox(), Terminal.escape);
    }

    public Request(
            Option<Component> background,
            TextComponent component,
            Predicate<KeyStroke> end
    ) {
        this.background = background;
        this.component = component;
        this.end = end;
    }

    public Request withBackground(Component background) {
        return new Request(Option.some(background), component, end);
    }

    public Request withComponent(TextComponent component) {
        return new Request(background, component, end);
    }

    public Request withEnd(Predicate<KeyStroke> end) {
        return new Request(background, component, end);
    }

    public IO<String> run() {
        return runT(component).map(Trampoline::run);
    }

    private IO<Trampoline<String>> runT(TextComponent component) {
        return IO.empty
                .flatMap(x -> background.isSome() ? background.some().render() : IO.pure(x))
                .flatMap(component::render)
                .flatMap(Terminal::refresh)
                .flatMap(Terminal::read)
                .flatMap(in -> {
                    if (end.test(in)) {
                        return Terminal.resetCursor().map(x -> Trampoline.pure(component.text()));
                    } else {
                        return IO.pure(Trampoline.suspend(new P1<Trampoline<String>>() {
                            @Override
                            public Trampoline<String> _1() {
                                return runT(component.accept(in)).runUnsafe();
                            }
                        }));
                    }
                });
    }
}
