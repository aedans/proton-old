package io.github.aedans.proton.system.json;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.aedans.proton.util.Settings;

public final class JsonRendererSettings {
    public static final JsonRendererSettings settings = Settings.get(JsonRendererSettings.class);
    public TextColor stringColor = TextColor.ANSI.GREEN;
    public TextColor intColor = TextColor.ANSI.BLUE;
    public TextColor boolColor = TextColor.ANSI.YELLOW;
    public TextColor nullColor = TextColor.ANSI.RED;
    public TextColor fieldNameColor = TextColor.ANSI.MAGENTA;
    public TextCharacter[] fieldValueSeparator = {new TextCharacter(':')};
    public TextCharacter[] objectFieldSeparator = {new TextCharacter(',')};
    public TextCharacter[] arrayValueSeparator = {new TextCharacter(',')};
    public TextCharacter[] beginJsonObject = {new TextCharacter('{')};
    public TextCharacter[] endJsonObject = {new TextCharacter('}')};
    public TextCharacter[] beginJsonArray = {new TextCharacter('[')};
    public TextCharacter[] endJsonArray = {new TextCharacter(']')};
}
