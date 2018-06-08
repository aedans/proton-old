package io.github.aedans.proton.system.directory;

import java.io.File;

import org.pf4j.Extension;

import io.github.aedans.proton.io.AstWriter;
import io.reactivex.Completable;

@Extension
public final class DirectoryWriter implements AstWriter<AbstractDirectory> {
	@Override
	public Class<? extends AbstractDirectory> key() {
		return AbstractDirectory.class;
	}

	@Override
    public Completable writeTo(AbstractDirectory directory, File file) {
		return Completable.fromRunnable(() -> {
            directory.contents().forEach((name, ast) -> {
                AstWriter.write(ast, new File(file, name));
            });
        });
	}
}