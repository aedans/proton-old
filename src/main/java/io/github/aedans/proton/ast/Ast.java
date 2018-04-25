package io.github.aedans.proton.ast;

import fj.data.Collectors;
import fj.data.Stream;
import io.github.aedans.pfj.IO;
import io.github.aedans.proton.system.directory.Directory;
import io.github.aedans.proton.system.text.TextFileAssociation;
import io.github.aedans.proton.util.FileUtils;
import io.github.aedans.proton.util.Key;
import io.github.aedans.proton.util.Plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public interface Ast {
    Key type();

    static IO<? extends Ast> from(File file) {
        if (file.isDirectory()) {
            return Directory.from(file);
        } else {
            try {
                String extension = FileUtils.extension(file);
                FileAssociation association = FileAssociation.from(extension);
                AstReader reader = Plugins.forKey(AstReader.class, association.astKey())
                        .valueE("Could not find ast reader for " + association.astKey());
                Stream<String> lines = new BufferedReader(new FileReader(file)).lines().collect(Collectors.toStream());
                return IO.pure(reader.read(lines));
            } catch (Throwable e) {
                return IO.pure(() -> { throw new RuntimeException(e); });
            }
        }
    }
}
