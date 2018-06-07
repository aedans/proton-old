package io.github.aedans.proton.util;

import io.github.aedans.proton.system.directory.DirectoryLoader;
import io.reactivex.Maybe;
import io.reactivex.Single;

import org.pf4j.ExtensionPoint;

import java.io.File;
import java.util.concurrent.Callable;

public interface Loader<A extends Ast> extends ExtensionPoint, ForClass<A> {
    Maybe<A> load(File file);

    default Single<A> loadOrIdentity(File file, Callable<? extends A> ast) {
        return load(file).switchIfEmpty(Single.fromCallable(ast));
    }  

    static Loader<Ast> global = Loader.of(Ast.class)
            .combine(new DirectoryLoader());

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

    default Loader<A> combine(Loader<? extends A> loader) {
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