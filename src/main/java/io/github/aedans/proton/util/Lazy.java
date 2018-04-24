package io.github.aedans.proton.util;

import java.util.function.Supplier;

public final class Lazy<T> implements Supplier<T> {
    private final Supplier<T> supplier;
    private T cache = null;

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        return cache == null ? (cache = supplier.get()) : cache;
    }
}
