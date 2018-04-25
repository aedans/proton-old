package io.github.aedans.proton.util;

public class Key {
    protected final String name;

    public Key(String name) {
        this.name = name;
    }

    public static Key unique(String name) {
        return new Key(name);
    }

    @Override
    public String toString() {
        return "Key(" + name + ")";
    }
}
