package io.github.aedans.proton.system.directory;

import fj.Ord;
import fj.data.List;
import fj.data.Option;
import fj.data.Seq;
import fj.data.TreeMap;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.util.AbstractImmutable;
import io.github.aedans.proton.util.IO;
import io.github.aedans.proton.util.Key;
import org.immutables.value.Value;

import java.io.File;
import java.util.function.Supplier;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractDirectory implements Ast {
    public static final Key key = Key.of(AbstractDirectory.class);

    public static Directory empty(File file) {
        return Directory.of(TreeMap.empty(Ord.stringOrd), file);
    }

    public static IO<Directory> from(File file) {
        return IO.run(() -> Seq.arraySeq(file.listFiles())
                .foldLeft((a, b) -> a.put(b.getName(), () -> Ast.from(b).runUnsafe()), empty(file)));
    }

    @Value.Parameter
    public abstract TreeMap<String, Supplier<Ast>> map();

    @Value.Parameter
    public abstract File file();

    @Override
    public Key type() {
        return key;
    }

    public Option<Ast> get(String name) {
        return name.equals(".") ? Option.some(this) : map().get(name).map(Supplier::get);
    }

    public List<String> names() {
        return map().keys();
    }

    public Directory put(String name, Supplier<Ast> resource) {
        return Directory.copyOf(this).withMap(map().set(name, resource));
    }

    public Option<Ast> get(Seq<String> path) {
        if (path.isEmpty()) {
            return Option.none();
        } else if (path.length() == 1) {
            return get(path.head());
        } else {
            Option<Ast> ast = get(path.head());
            if (ast.isSome()) {
                return ((Directory) ast.some()).get(path.tail());
            } else {
                return ast;
            }
        }
    }

    public Directory put(Seq<String> path, Supplier<Ast> resource) {
        if (path.isEmpty()) {
            return Directory.copyOf(this);
        } else if (path.length() == 1) {
            return put(path.head(), resource);
        } else {
            Option<Ast> dir = get(path.head());
            if (dir.isSome()) {
                return ((Directory) dir.some()).put(path.tail(), resource);
            } else {
                return this
                        .put(path.head(), () -> empty(new File(file(), path.head())))
                        .put(path.tail(), resource);
            }
        }
    }
}
