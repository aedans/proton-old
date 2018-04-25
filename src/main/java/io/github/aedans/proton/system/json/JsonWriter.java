package io.github.aedans.proton.system.json;

import com.eclipsesource.json.WriterConfig;
import fj.data.Collectors;
import fj.data.Stream;
import io.github.aedans.proton.ast.AstWriter;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

import java.io.BufferedReader;
import java.io.StringReader;

@Extension
public final class JsonWriter implements AstWriter<JsonAst> {
    @Override
    public Stream<String> write(JsonAst ast) {
        return new BufferedReader(new StringReader(ast.value().toString(WriterConfig.MINIMAL))).lines()
                .collect(Collectors.toStream());
    }

    @Override
    public Key key() {
        return null;
    }
}
