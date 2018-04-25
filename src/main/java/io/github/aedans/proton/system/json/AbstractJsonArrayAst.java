package io.github.aedans.proton.system.json;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import fj.data.Seq;
import io.github.aedans.proton.util.AbstractImmutable;
import org.immutables.value.Value;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractJsonArrayAst implements JsonAst {
    @Value.Default
    public Seq<JsonAst> elements() {
        return Seq.empty();
    }

    @Override
    public JsonValue value() {
        JsonArray array = Json.array();
        elements().forEach(x -> array.add(x.value()));
        return JsonArray.unmodifiableArray(array);
    }

    public JsonArrayAst with(Seq<JsonAst> seq) {
        return JsonArrayAst.copyOf(this).withElements(elements().append(seq));
    }
}
