package io.github.aedans.proton.util;

public final class Key<T> {
    private final Class<T> clazz;

    private Key(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T> Key<T> of(Class<T> clazz) {
        return new Key<>(clazz);
    }

    @Override
    public String toString() {
        return "Key(" + clazz.getSimpleName() + ")";
    }
}
