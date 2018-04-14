package io.github.aedans.proton.ast;

import io.github.aedans.proton.logic.Plugins;
import io.github.aedans.proton.util.FileUtils;
import io.github.aedans.proton.util.Key;

import java.io.File;
import java.io.FileReader;

import static io.github.aedans.proton.util.ExceptionUtils.doUnchecked;

public interface Resource {
    static Resource from(File file) {
        if (file.isDirectory()) {
            return Directory.from(file);
        } else {
            try {
                String extension = FileUtils.extension(file);
                FileAssociation association = Plugins.forKey(FileAssociation.class, new Key.ForString(extension))
                        .valueE("Could not find path association for extension ." + extension);
                AstReader reader = Plugins.forKey(AstReader.class, association.astKey())
                        .valueE("Could not find ast reader for " + association.astKey());
                return doUnchecked(() -> reader.read(new FileReader(file)));
            } catch (Throwable e) {
                return new Ast() {
                    @Override
                    public Key type() {
                        return Key.unique(e.getMessage());
                    }
                };
            }
        }
    }
}
