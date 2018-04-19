package io.github.aedans.proton.system.text;

import fj.data.Stream;
import io.github.aedans.proton.ast.AstWriter;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class TextWriter implements AstWriter<Text> {
    @Override
    public Key key() {
        return Text.key;
    }

    @Override
    public Stream<String> write(Text text) {
        return text.text.map(TextString::toString);
    }
}
