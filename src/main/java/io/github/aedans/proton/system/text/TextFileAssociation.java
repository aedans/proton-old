package io.github.aedans.proton.system.text;

import io.github.aedans.proton.ast.FileAssociation;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class TextFileAssociation implements FileAssociation {
    @Override
    public String extension() {
        return "txt";
    }

    @Override
    public Key key() {
        return Text.key;
    }
}
