package io.github.aedans.proton.lanterna;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.aedans.proton.util.Ast;
import io.github.aedans.proton.util.ForClass;
import io.github.aedans.proton.util.Plugins;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.pf4j.ExtensionPoint;

public interface KeyListener<A extends Ast> extends ExtensionPoint, ForClass<A> {
    Maybe<A> accept(KeyStroke key, A a);

    default Single<A> acceptOrIdentity(KeyStroke key, A a) {
        return accept(key, a).toSingle(a);
    }

    @SuppressWarnings("unchecked")
    static <A extends Ast> KeyListener<A> of(Class<? extends A> key) {
        return Plugins.all(KeyListener.class)
                .map(x -> ((KeyListener<A>) x))
                .filter(x -> x.key().isAssignableFrom(key))
                .fold(KeyListener.empty(key), KeyListener::combine);
    }

    static <A extends Ast> KeyListener<A> empty(Class<? extends A> clazz) {
        return new KeyListener<A>() {
            @Override
            public Class<? extends A> key() {
                return clazz;
            }

            @Override
            public Maybe<A> accept(KeyStroke key, A a) {
                return Maybe.empty();
            }
        };
    }

    default KeyListener<A> combine(KeyListener<A> keyListener) {
        return new KeyListener<A>() {
            @Override
            public Class<? extends A> key() {
                return KeyListener.this.key();
            }

            @Override
            public Maybe<A> accept(KeyStroke key, A a) {
                return KeyListener.this.accept(key, a).switchIfEmpty(keyListener.accept(key, a));
            }
        };
    }
}