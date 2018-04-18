package io.github.aedans.proton.util;

public class Key {
    protected final String name;

    public Key(String name) {
        this.name = name;
    }

    public static Key unique(String name) {
        return new Key(name);
    }

    public static final class ForString extends Key {
        public ForString(String name) {
            super(name);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ForString && ((ForString) obj).name.equals(name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    @Override
    public String toString() {
        return "Key{" +
                "name='" + name + '\'' +
                '}';
    }
}
