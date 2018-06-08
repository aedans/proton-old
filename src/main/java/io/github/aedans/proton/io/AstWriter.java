package io.github.aedans.proton.io;

import java.io.File;

import org.pf4j.ExtensionPoint;

import io.github.aedans.proton.util.Ast;
import io.github.aedans.proton.util.ForClass;
import io.github.aedans.proton.util.Plugins;
import io.reactivex.Completable;

public interface AstWriter<A extends Ast> extends ExtensionPoint, ForClass<A> {
    Completable writeTo(A a, File file);

    @SuppressWarnings("unchecked")
    static <A extends Ast> Completable write(A a, File file) {
        return AstWriter.of((Class<A>) a.getClass()).writeTo(a, file);
    }

    @SuppressWarnings("unchecked")
    static <A extends Ast> AstWriter<A> of(Class<? extends A> key) {
        return Plugins.all(AstWriter.class)
                .toStream()
                .map(x -> ((AstWriter<A>) x))
                .filter(x -> key.isAssignableFrom(x.key()))
                .headOption()
                .getOrElse(() -> empty(key));
    }

    static <A extends Ast> AstWriter<A> empty(Class<? extends A> key) {
        return new AstWriter<A>() {
            @Override
            public Completable writeTo(A a, File file) {
                return Completable.complete();
            }

			@Override
			public Class<? extends A> key() {
				return key;
			}
        };
    }
}