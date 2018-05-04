package io.github.aedans.proton.system.json;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import fj.Ord;
import fj.P2;
import fj.data.List;
import fj.data.Seq;
import fj.data.TreeMap;
import io.github.aedans.proton.util.AbstractImmutable;
import org.immutables.value.Value;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractJsonObjectAst implements JsonAst {
    @Value.Default
    public TreeMap<String, JsonAst> map() {
        return TreeMap.empty(Ord.stringOrd);
    }

    @Value.Default
    public int selected() {
        return 0;
    }

    @Value.Check
    public AbstractJsonObjectAst normalize() {
        if (selected() < 0) {
            return JsonObjectAst.copyOf(this).withSelected(0);
        }
        int size = Math.max(0, map().toList().length() - 1);
        if (selected() > size) {
            return JsonObjectAst.copyOf(this).withSelected(size);
        } else {
            return this;
        }
    }

    @Override
    public JsonObject value() {
        JsonObject object = Json.object();
        map().forEach(x -> object.add(x._1(), x._2().value()));
        return JsonObject.unmodifiableObject(object);
    }

    public JsonObjectAst with(String name, JsonAst ast) {
        return JsonObjectAst.copyOf(this).withMap(map().set(name, ast));
    }

    public JsonObjectAst with(JsonObject object) {
        return Seq.fromJavaList(object.names()).foldLeft(
                (a, b) -> a.with(b, JsonAst.from(object.get(b))),
                (JsonObjectAst) this
        );
    }
}
