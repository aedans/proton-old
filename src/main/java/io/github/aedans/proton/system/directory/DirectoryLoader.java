package io.github.aedans.proton.system.directory;

import java.io.File;

import io.github.aedans.proton.util.Ast;
import io.github.aedans.proton.util.Loader;
import io.github.aedans.proton.util.Unit;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

public final class DirectoryLoader implements Loader<AbstractDirectory> {
    @Override
    public Class<? extends AbstractDirectory> key() {
        return AbstractDirectory.class;
    }

    @Override
	public Maybe<AbstractDirectory> load(File file) {
        if (file.isDirectory()) {
            Stream<File> files = Stream.of(file.listFiles());
            Map<String, Ast> map = files
                    .toMap(f -> f.getName(), f -> Loader.global.load(f))
                    .mapValues(f -> f.switchIfEmpty(Single.just(Unit.unit)).blockingGet());
            return Maybe.just(Directory.of(map));
        } else {
            return Maybe.empty();
        }
	}
}