package io.github.aedans.proton.system.json;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import fj.P2;
import fj.data.List;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ui.AstRenderer;
import io.github.aedans.proton.ui.AstRendererResult;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import io.github.aedans.proton.util.pretty.PrettyFormatter;
import org.pf4j.Extension;

import static io.github.aedans.proton.util.pretty.PrettyFormatter.*;

@Extension
public final class JsonRenderer implements AstRenderer<JsonAst> {
    @Override
    public AstRendererResult render(JsonAst ast, TerminalSize size) {
        return formatter(ast).format(size.getColumns());
    }

    public PrettyFormatter formatter(JsonAst ast) {
        if (ast instanceof AbstractJsonObjectAst) {
            AbstractJsonObjectAst jsonObjectAst = (AbstractJsonObjectAst) ast;
            int selected = jsonObjectAst.selected();
            Stream<PrettyFormatter> variableFormatters = jsonObjectAst.map().toStream()
                    .zipIndex()
                    .map(x -> {
                        P2<String, JsonAst> value = x._1();
                        Integer index = x._2();
                        return combine(
                                index == selected ? newline.withCursor() : newline,
                                text(TextString.fromString('"' + value._1() + '"')
                                        .map(c -> c.withForegroundColor(TextColor.ANSI.MAGENTA))),
                                text(new TextCharacter(':')),
                                formatter(value._2())
                        );
                    });
            List<PrettyFormatter> elementFormatters = variableFormatters
                    .toList()
                    .intersperse(text(new TextCharacter(',')));
            return combine(
                    text(new TextCharacter('{')),
                    combine(elementFormatters).indent(2).combine(newline).group(),
                    text(new TextCharacter('}'))
            );
        } else if (ast instanceof AbstractJsonArrayAst) {
            AbstractJsonArrayAst jsonArrayAst = (AbstractJsonArrayAst) ast;
            List<PrettyFormatter> elementFormatters = jsonArrayAst.elements()
                    .map(x -> linebreak.combine(formatter(x)))
                    .toList()
                    .intersperse(text(new TextCharacter(',')));
            return combine(
                    text(new TextCharacter('[')),
                    combine(elementFormatters).indent(2).combine(linebreak).group(),
                    text(new TextCharacter(']'))
            );
        } else if (ast instanceof AbstractJsonStringAst) {
            AbstractJsonStringAst jsonStringAst = (AbstractJsonStringAst) ast;
            String s = jsonStringAst.value().toString();
            return text(TextString.fromString(s).map(x -> x.withForegroundColor(TextColor.ANSI.GREEN)));
        } else if (ast instanceof AbstractJsonNumberAst) {
            AbstractJsonNumberAst jsonNumberAst = (AbstractJsonNumberAst) ast;
            String s = jsonNumberAst.value().toString();
            return text(TextString.fromString(s).map(x -> x.withForegroundColor(TextColor.ANSI.BLUE)));
        } else if (ast instanceof AbstractJsonBooleanAst) {
            AbstractJsonBooleanAst jsonBooleanAst = (AbstractJsonBooleanAst) ast;
            String s = jsonBooleanAst.value().toString();
            return text(TextString.fromString(s).map(x -> x.withForegroundColor(TextColor.ANSI.YELLOW)));
        } else if (ast instanceof AbstractJsonNullAst) {
            AbstractJsonNullAst jsonNullAst = (AbstractJsonNullAst) ast;
            String s = jsonNullAst.value().toString();
            return text(TextString.fromString(s).map(x -> x.withForegroundColor(TextColor.ANSI.YELLOW)));
        } else {
            throw new RuntimeException("Unrecognized value " + ast);
        }
    }

    @Override
    public Key key() {
        return JsonAst.key;
    }
}
