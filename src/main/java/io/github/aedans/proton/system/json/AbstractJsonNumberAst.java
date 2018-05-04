package io.github.aedans.proton.system.json;

import com.eclipsesource.json.JsonValue;
import io.github.aedans.proton.util.AbstractImmutable;
import org.immutables.value.Value;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractJsonNumberAst implements JsonAst {
    @Value.Parameter
    public abstract JsonValue number();

    @Override
    public JsonValue value() {
        return number();
    }
}
