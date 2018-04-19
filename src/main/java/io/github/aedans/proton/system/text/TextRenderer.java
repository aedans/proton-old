package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ui.AstRenderer;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class TextRenderer implements AstRenderer<Text> {
    @Override
    public Stream<Seq<TextCharacter>> render(Text text, TerminalSize size) {
        return text.text.toStream();
    }

    @Override
    public TerminalPosition cursor(Text text, TerminalSize size) {
        return text.cursor;
    }

    @Override
    public Key key() {
        return Text.key;
    }
}
