package io.github.aedans.proton.system.json;

import com.eclipsesource.json.JsonValue;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.util.Key;

public interface JsonAst extends Ast {
    Key key = Key.of(JsonAst.class);

    JsonValue value();

    static JsonAst from(JsonValue value) {
        if (value.isObject()) {
            return JsonObjectAst.builder().build().with(value.asObject());
        } else if (value.isArray()) {
            return JsonArrayAst.builder().build().with(value.asArray());
        } else if (value.isString()) {
            return JsonStringAst.of(value.asString());
        } else if (value.isBoolean()) {
            return JsonBooleanAst.of(value.asBoolean());
        } else if (value.isNumber()) {
            return JsonNumberAst.of(value);
        } else if (value.isNull()) {
            return JsonNullAst.of();
        } else {
            throw new RuntimeException("Unrecognized value " + value);
        }
    }

    @Override
    default Key type() {
        return key;
    }
}
