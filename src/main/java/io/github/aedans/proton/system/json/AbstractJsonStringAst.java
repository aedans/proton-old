package io.github.aedans.proton.system.json;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import io.github.aedans.proton.util.AbstractImmutable;
import org.immutables.value.Value;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractJsonStringAst implements JsonAst {
    @Value.Parameter
    public abstract String string();

    @Override
    public JsonValue value() {
        return Json.value(string());
    }
}
