package io.github.aedans.proton.system.json;

import com.eclipsesource.json.Json;
import fj.data.Stream;
import io.github.aedans.proton.ast.AstReader;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class JsonReader implements AstReader<JsonAst> {
    @Override
    public JsonAst read(Stream<String> input) {
        return JsonAst.from(Json.parse(input.foldLeft1((a, b) -> a + b)).asObject());
    }

    @Override
    public Key key() {
        return JsonAst.key;
    }
}
