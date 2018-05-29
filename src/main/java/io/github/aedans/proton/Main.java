package io.github.aedans.proton;

import com.googlecode.lanterna.TerminalPosition;
import io.github.aedans.proton.lanterna.KeyListener;
import io.github.aedans.proton.lanterna.Renderer;
import io.github.aedans.proton.lanterna.Terminal;
import io.github.aedans.proton.util.Ast;
import io.github.aedans.proton.util.Plugins;
import io.github.aedans.proton.util.Trampoline;
import io.github.aedans.proton.util.Unit;
import io.reactivex.Completable;
import io.reactivex.Single;

public class Main {
    public static void main(String[] args) {
        Completable.complete()
                 .andThen(Plugins.start())
                 .andThen(Terminal.start())
                 .toSingle(() -> run(Unit.unit))
                 .flatMap(x -> x.map(Trampoline::get))
                 .ignoreElement()
                 .andThen(Plugins.stop())
                 .andThen(Terminal.stop())
                 .blockingAwait();
    }

    private static Single<Trampoline<Unit>> run(Ast ast) {
        return Completable.complete()
                .andThen(Terminal.write(Renderer.ofOrEmpty(ast).render(ast), TerminalPosition.TOP_LEFT_CORNER))
                .andThen(Terminal.refresh())
                .andThen(Terminal.read())
                .map(input -> Trampoline
                        .defer(() -> run(KeyListener.<Ast>of(ast.getClass()).acceptOrIdentity(input, ast).blockingGet())
                                .blockingGet()))
                .toSingle(Trampoline.pure(Unit.unit));
    }
}