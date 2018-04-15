package io.github.aedans.pfj;

import java.util.Objects;

public final class Product<A, B> {
    public final A a;
    public final B b;

    public Product(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        return "(" + a + ", " + b + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product<?, ?> product = (Product<?, ?>) o;
        return Objects.equals(a, product.a) &&
                Objects.equals(b, product.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
