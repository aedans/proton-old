package io.github.aedans.pfj;

import fj.Unit;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IO<A> {
    A run() throws Throwable;

    default A runUnsafe() {
        try {
            return run();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    default <B> IO<B> map(Function<A, B> fn) {
        return () -> fn.apply(run());
    }

    default <B> IO<B> ap(IO<Function<A, B>> fn) {
        return () -> fn.run().apply(run());
    }

    default <B> IO<B> flatMap(Supplier<IO<B>> fn) {
        return flatMap(x -> fn.get());
    }

    default <B> IO<B> flatMap(Function<A, IO<B>> fn) {
        return () -> fn.apply(run()).run();
    }

    IO<Unit> empty = IO.run(Unit::unit);

    static <B> IO<B> run(IO<B> fn) {
        return fn;
    }

    static IO<Unit> run(CheckedRunnable fn) {
        return () -> {
            fn.run();
            return Unit.unit();
        };
    }

    static <A> IO<A> pure(A a) {
        return run(() -> a);
    }

    interface CheckedRunnable {
        void run() throws Throwable;
    }
}
