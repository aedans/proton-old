package io.github.aedans.proton.system.text;

import fj.data.Stream;
import io.github.aedans.proton.ast.AstReader;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class TextReader implements AstReader<Text> {
    @Override
    public Key key() {
        return Text.key;
    }

    @Override
    public Text read(Stream<String> input) {
        return Text.builder().text(input.map(TextString::fromString)).build();
    }
}
