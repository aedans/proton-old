package io.github.aedans.proton.util;

import io.reactivex.Maybe;
import org.pf4j.ExtensionPoint;

import java.io.File;

public interface Loader<A extends Ast> extends ExtensionPoint, ForClass<A> {
    Maybe<A> load(File file);

    @SuppressWarnings("unchecked")
    static <A extends Ast> Loader<A> of(Class<? extends A> key) {
        return Plugins.all(Loader.class)
                .map(x -> ((Loader<A>) x))
                .filter(x -> key.isAssignableFrom(x.key()))
                .fold(Loader.empty(key), Loader::combine);
    }

    static <A extends Ast> Loader<A> empty(Class<? extends A> key) {
        return new Loader<A>() {
            @Override
            public Maybe<A> load(File file) {
                return Maybe.empty();
            }

            @Override
            public Class<? extends A> key() {
                return key;
            }
        };
    }

    default Loader<A> combine(Loader<A> loader) {
        return new Loader<A>() {
            @Override
            public Maybe<A> load(File file) {
                return Loader.this.load(file).switchIfEmpty(loader.load(file));
            }

            @Override
            public Class<? extends A> key() {
                return Loader.this.key();
            }
        };
    }
}