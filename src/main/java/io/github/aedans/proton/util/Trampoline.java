package io.github.aedans.proton.util;

import java.util.function.Supplier;

public abstract class Trampoline<A> {
    public static <A> Trampoline<A> pure(A a) {
        return new Pure<>(a);
    }

    public static <A> Trampoline<A> defer(Supplier<Trampoline<A>> a) {
        return new Defer<>(a);
    }
    
    public A get() {
        Trampoline<A> ta = this;
        while (ta instanceof Defer) {
            ta = ((Defer<A>) ta).a.get();
        }
        return ((Pure<A>) ta).a;
    }

    private static final class Pure<A> extends Trampoline<A> {
        private final A a;

        public Pure(A a) {
            this.a = a;
        }
    }

    private static final class Defer<A> extends Trampoline<A> {
        private final Supplier<Trampoline<A>> a;

        public Defer(Supplier<Trampoline<A>> a) {
            this.a = a;
        }
    }
}