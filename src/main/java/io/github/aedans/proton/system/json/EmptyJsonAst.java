package io.github.aedans.proton.system.json;

import io.github.aedans.proton.system.proton.New;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class EmptyJsonAst implements New.Empty<JsonAst> {
    @Override
    public JsonAst create() {
        return JsonObjectAst.builder().build();
    }

    @Override
    public Key key() {
        return JsonAst.key;
    }
}
