package io.github.aedans.proton.system.directory;

import fj.Ord;
import fj.data.List;
import fj.data.Option;
import fj.data.Seq;
import fj.data.TreeMap;
import io.github.aedans.pfj.IO;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.util.Key;

import java.io.File;
import java.util.function.Supplier;

public final class Directory implements Ast {
    public static final Key key = Key.unique("directory");

    private final TreeMap<String, Supplier<Ast>> map;
    private final File file;

    public Directory(TreeMap<String, Supplier<Ast>> map, File file) {
        this.map = map;
        this.file = file;
    }

    @Override
    public Key type() {
        return key;
    }

    public Option<Ast> get(String name) {
        return name.equals(".") ? Option.some(this) : map.get(name).map(Supplier::get);
    }

    public List<String> getNames() {
        return map.keys();
    }

    public Directory put(String name, Supplier<Ast> resource) {
        return new Directory(map.set(name, resource), file);
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
            return this;
        } else if (path.length() == 1) {
            return put(path.head(), resource);
        } else {
            Option<Ast> dir = get(path.head());
            if (dir.isSome()) {
                return ((Directory) dir.some()).put(path.tail(), resource);
            } else {
                return this
                        .put(path.head(), () -> empty(new File(file, path.head())))
                        .put(path.tail(), resource);
            }
        }
    }

    public File file() {
        return file;
    }

    public static Directory empty(File file) {
        return new Directory(TreeMap.empty(Ord.stringOrd), file);
    }

    public static IO<Directory> from(File file) {
        return IO.run(() -> Seq.arraySeq(file.listFiles())
                .foldLeft((a, b) -> a.put(b.getName(), () -> Ast.from(b).runUnsafe()), empty(file)));
    }
}
