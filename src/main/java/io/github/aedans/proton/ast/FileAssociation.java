package io.github.aedans.proton.ast;

import fj.Ord;
import fj.P;
import fj.data.List;
import fj.data.TreeMap;
import io.github.aedans.proton.util.Key;
import io.github.aedans.proton.util.Plugins;
import org.pf4j.ExtensionPoint;

public interface FileAssociation extends ExtensionPoint {
    List<FileAssociation> allFileAssociations = Plugins.all(FileAssociation.class);
    TreeMap<String, Key> fileAssociations = TreeMap.iterableTreeMap(
            Ord.stringOrd,
            allFileAssociations.map(x -> P.p(x.extension(), x.key()))
    );

    static Key from(String extension) {
        return fileAssociations.get(extension).valueE("Could not find file extension " + extension);
    }

    String extension();

    Key key();
}
