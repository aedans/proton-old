package io.github.aedans.proton.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.googlecode.lanterna.TextColor;
import org.pf4j.Extension;

@Extension
public final class TextColorGsonExtension implements Settings.GsonExtension {
    @Override
    public void build(GsonBuilder builder) {
        builder.registerTypeAdapter(TextColor.class, (JsonSerializer<TextColor>) (src, typeOfSrc, context) ->
                new JsonPrimitive(src.toString()));

        builder.registerTypeAdapter(TextColor.class, (JsonDeserializer<TextColor>) (json, typeOfT, context) ->
                TextColor.Factory.fromString(json.getAsString()));
    }
}
