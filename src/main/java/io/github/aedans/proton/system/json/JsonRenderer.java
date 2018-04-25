package io.github.aedans.proton.system.json;

import com.eclipsesource.json.WriterConfig;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Collectors;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ui.AstRenderer;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

import java.io.BufferedReader;
import java.io.StringReader;

@Extension
public final class JsonRenderer implements AstRenderer<JsonAst> {
    @Override
    public Stream<Seq<TextCharacter>> render(JsonAst ast, TerminalSize size) {
        return new BufferedReader(new StringReader(ast.value().toString(WriterConfig.PRETTY_PRINT))).lines()
                .collect(Collectors.toStream())
                .map(TextString::fromString);
    }

    @Override
    public TerminalPosition cursor(JsonAst ast, TerminalSize size) {
        return TerminalPosition.TOP_LEFT_CORNER;
    }

    @Override
    public Key key() {
        return JsonAst.key;
    }
}
