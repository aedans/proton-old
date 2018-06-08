package io.github.aedans.proton.system.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import io.github.aedans.proton.io.AstReader;
import io.reactivex.Maybe;
import io.vavr.collection.Stream;

public final class TextReader implements AstReader<AbstractText> {
	@Override
	public Class<? extends AbstractText> key() {
		return AbstractText.class;
	}

	@Override
	public Maybe<AbstractText> read(File file) {
		if (file.isFile()) {
            return Maybe.fromCallable(() -> {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    return Text.of(Stream.ofAll(reader.lines()));    
                }
            });
        } else {
            return Maybe.empty();
        }
	}
}