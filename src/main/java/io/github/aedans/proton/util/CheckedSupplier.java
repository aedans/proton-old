package io.github.aedans.proton.util;

@FunctionalInterface
public interface CheckedSupplier<T> {
    T get() throws Throwable;
}