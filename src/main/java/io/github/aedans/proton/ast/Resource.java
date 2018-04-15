package io.github.aedans.proton.ast;

import fj.data.Stream;
import io.github.aedans.pfj.IO;
import io.github.aedans.proton.logic.Plugins;
import io.github.aedans.proton.system.text.TextFileAssociation;
import io.github.aedans.proton.util.FileUtils;
import io.github.aedans.proton.util.Key;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public interface Resource {
    static IO<? extends Resource> from(File file) {
        if (file.isDirectory()) {
            return Directory.from(file);
        } else {
            try {
                String extension = FileUtils.extension(file);
                FileAssociation association = Plugins.forKey(FileAssociation.class, new Key.ForString(extension))
                        .orSome(new TextFileAssociation());
                AstReader reader = Plugins.forKey(AstReader.class, association.astKey())
                        .valueE("Could not find ast reader for " + association.astKey());
                return IO.pure(reader.read(Stream.iteratorStream(new BufferedReader(new FileReader(file)).lines().iterator())));
            } catch (Throwable e) {
                return IO.pure((Ast) () -> Key.unique(e.getMessage()));
            }
        }
    }
}
