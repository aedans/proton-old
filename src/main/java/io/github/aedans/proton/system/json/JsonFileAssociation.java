package io.github.aedans.proton.system.json;

import io.github.aedans.proton.ast.FileAssociation;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class JsonFileAssociation implements FileAssociation {
    @Override
    public String extension() {
        return "json";
    }

    @Override
    public Key key() {
        return JsonAst.key;
    }
}
