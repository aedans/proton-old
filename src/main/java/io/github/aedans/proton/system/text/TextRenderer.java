package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.TerminalSize;
import io.github.aedans.proton.ui.AstRenderer;
import io.github.aedans.proton.ui.AstRendererResult;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class TextRenderer implements AstRenderer<Text> {
    @Override
    public AstRendererResult render(Text text, TerminalSize size) {
        return AstRendererResult.builder()
                .text(text.text()
                        .drop(text.scroll().getRows())
                        .map(line -> line.drop(text.scroll().getColumns())))
                .cursor(text.cursor())
                .build();
    }

    @Override
    public String entry(Text ast) {
        return TextString.toString(ast.text());
    }

    @Override
    public Key key() {
        return Text.key;
    }
}
