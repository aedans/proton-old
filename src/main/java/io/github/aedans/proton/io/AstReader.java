package io.github.aedans.proton.io;

import io.github.aedans.proton.system.directory.DirectoryLoader;
import io.github.aedans.proton.system.text.TextLoader;
import io.github.aedans.proton.util.Ast;
import io.github.aedans.proton.util.ForClass;
import io.github.aedans.proton.util.Plugins;
import io.reactivex.Maybe;
import io.reactivex.Single;

import org.pf4j.ExtensionPoint;

import java.io.File;
import java.util.concurrent.Callable;

public interface AstReader<A extends Ast> extends ExtensionPoint, ForClass<A> {
    Maybe<A> read(File file);

    default Single<A> loadOrIdentity(File file, Callable<? extends A> ast) {
        return read(file).switchIfEmpty(Single.fromCallable(ast));
    }

    static AstReader<Ast> global = AstReader.of(Ast.class)
            .combine(new DirectoryLoader())
            .combine(new TextLoader());

    @SuppressWarnings("unchecked")
    static <A extends Ast> AstReader<A> of(Class<? extends A> key) {
        return Plugins.all(AstReader.class)
                .map(x -> ((AstReader<A>) x))
                .filter(x -> key.isAssignableFrom(x.key()))
                .fold(AstReader.empty(key), AstReader::combine);
    }

    static <A extends Ast> AstReader<A> empty(Class<? extends A> key) {
        return new AstReader<A>() {
            @Override
            public Maybe<A> read(File file) {
                return Maybe.empty();
            }

            @Override
            public Class<? extends A> key() {
                return key;
            }
        };
    }

    default AstReader<A> combine(AstReader<? extends A> loader) {
        return new AstReader<A>() {
            @Override
            public Maybe<A> read(File file) {
                return AstReader.this.read(file).switchIfEmpty(loader.read(file));
            }

            @Override
            public Class<? extends A> key() {
                return AstReader.this.key();
            }
        };
    }
}