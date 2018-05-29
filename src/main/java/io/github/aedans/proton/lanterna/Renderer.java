package io.github.aedans.proton.lanterna;

import io.github.aedans.proton.util.Ast;
import io.github.aedans.proton.util.ForClass;
import io.github.aedans.proton.util.Plugins;
import io.reactivex.Observable;
import io.vavr.control.Option;

import org.pf4j.ExtensionPoint;

public interface Renderer<A extends Ast> extends ExtensionPoint, ForClass<A> {
    Observable<TextString> render(A a);

    @SuppressWarnings("unchecked")
    static <A extends Ast> Option<Renderer<A>> of(A ast) {
        return Plugins.all(Renderer.class)
                .map(x -> ((Renderer<A>) x))
                .filter(x -> x.key().isInstance(ast))
                .singleOption();
    }

    @SuppressWarnings("unchecked")
    static <A extends Ast> Renderer<A> ofOrEmpty(A ast) {
        return of(ast).getOrElse(() -> empty((Class<A>) ast.getClass()));
    }

    static <A extends Ast> Renderer<A> empty(Class<? extends A> key) {
        return new Renderer<A>() {
            @Override
            public Observable<TextString> render(A a) {
                return Observable.empty();
            }

            @Override
            public Class<? extends A> key() {
                return key;
            }
        };
    }
}