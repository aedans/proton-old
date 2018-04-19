package io.github.aedans.proton.ui;

import com.googlecode.lanterna.input.KeyStroke;
import fj.P;
import fj.Unit;
import fj.control.Trampoline;
import fj.data.Option;
import io.github.aedans.pfj.IO;
import io.github.aedans.proton.system.text.Text;

import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Request {
    private final Option<Renderable> background;
    private final Editor editor;
    private final Predicate<KeyStroke> end;

    public Request() {
        this(Option.none(), new Editor<>(new Text()), Terminal.escape);
    }

    public Request(
            Option<Renderable> background,
            Editor editor,
            Predicate<KeyStroke> end
    ) {
        this.background = background;
        this.editor = editor;
        this.end = end;
    }

    public Request withBackground(Renderable background) {
        return new Request(Option.some(background), editor, end);
    }

    public Request withEditor(Editor editor) {
        return new Request(background, editor, end);
    }

    public Request withEnd(Predicate<KeyStroke> end) {
        return new Request(background, editor, end);
    }

    public IO<String> run() {
        return runT(editor).map(Trampoline::run);
    }

    private IO<Trampoline<String>> runT(Editor editor) {
        return IO.empty
                .flatMap(x -> background.isSome() ? background.some().render() : IO.pure(x))
                .flatMap((Supplier<IO<Unit>>) editor::render)
                .flatMap(Terminal::refresh)
                .flatMap(Terminal::read)
                .flatMap(in -> {
                    if (end.test(in)) {
                        return Terminal.resetCursor().map(x -> Trampoline.pure(editor.text()));
                    } else {
                        return IO.pure(Trampoline.suspend(P.lazy(() -> runT(editor.accept(in)).runUnsafe())));
                    }
                });
    }
}
