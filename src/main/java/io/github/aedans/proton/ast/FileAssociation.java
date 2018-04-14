package io.github.aedans.proton.ast;

import io.github.aedans.proton.util.Key;
import io.github.aedans.proton.util.Unique;
import org.pf4j.ExtensionPoint;

public interface FileAssociation extends ExtensionPoint, Unique {
    String extension();

    Key astKey();

    @Override
    default Key key() {
        return new Key.ForString(extension());
    }
}
