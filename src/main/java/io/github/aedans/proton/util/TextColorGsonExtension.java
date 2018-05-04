package io.github.aedans.proton.util;

import com.google.gson.*;
import com.googlecode.lanterna.TextColor;
import org.pf4j.Extension;

import java.lang.reflect.Type;

@Extension
public class TextColorGsonExtension implements Settings.GsonExtension {
    @Override
    public void build(GsonBuilder builder) {
        builder.registerTypeAdapter(TextColor.class, (JsonSerializer<TextColor>) (src, typeOfSrc, context) ->
                new JsonPrimitive(src.toString()));

        builder.registerTypeAdapter(TextColor.class, (JsonDeserializer<TextColor>) (json, typeOfT, context) ->
                TextColor.Factory.fromString(json.getAsString()));
    }
}
