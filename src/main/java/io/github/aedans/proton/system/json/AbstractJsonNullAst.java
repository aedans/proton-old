package io.github.aedans.proton.system.json;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import io.github.aedans.proton.util.AbstractImmutable;
import org.immutables.value.Value;

@Value.Immutable(singleton = true)
@AbstractImmutable
public abstract class AbstractJsonNullAst implements JsonAst {
    @Override
    public JsonValue value() {
        return Json.NULL;
    }
}
