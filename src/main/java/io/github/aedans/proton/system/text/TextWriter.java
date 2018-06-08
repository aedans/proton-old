package io.github.aedans.proton.system.text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.pf4j.Extension;

import io.github.aedans.proton.io.AstWriter;
import io.reactivex.Completable;

@Extension
public final class TextWriter implements AstWriter<AbstractText> {
	@Override
	public Class<? extends AbstractText> key() {
		return AbstractText.class;
	}

	@Override
	public Completable writeTo(AbstractText a, File file) {
		return Completable.fromAction(() -> {
			try (FileWriter writer = new FileWriter(file)) {
				a.lines().forEach(x -> {
					try {
						writer.write(x);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
			}
        });
	}
}