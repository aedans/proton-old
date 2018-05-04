package io.github.aedans.proton.system.json;

import com.googlecode.lanterna.TerminalSize;
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

import static io.github.aedans.proton.system.json.JsonRendererSettings.*;
import static io.github.aedans.proton.util.pretty.PrettyFormatter.*;

@Extension
public final class JsonRenderer implements AstRenderer<JsonAst> {
    @Override
    public AstRendererResult render(JsonAst ast, TerminalSize size) {
        return formatter(ast, true).format(size.getColumns());
    }

    public PrettyFormatter formatter(JsonAst ast, boolean cursor) {
        if (ast instanceof AbstractJsonObjectAst) {
            AbstractJsonObjectAst jsonObjectAst = (AbstractJsonObjectAst) ast;
            Seq<PrettyFormatter> variableFormatters = Seq.iterableSeq(jsonObjectAst.map().toStream())
                    .map(value -> combine(
                            text(TextString.fromString('"' + value._1() + '"')
                                    .map(c -> c.withForegroundColor(settings.fieldNameColor))),
                            text(settings.fieldValueSeparator),
                            formatter(value._2(), false)
                    ));
            int selected = jsonObjectAst.selected();
            Seq<PrettyFormatter> update = !cursor || selected < 0 || selected > jsonObjectAst.list().length() - 1 ? variableFormatters : variableFormatters
                    .update(selected, variableFormatters.index(selected).withCursor());
            List<PrettyFormatter> elementFormatters = update
                    .toList()
                    .intersperse(text(settings.objectFieldSeparator).combine(newline));
            return combine(
                    text(settings.beginJsonObject),
                    newline.combine(combine(elementFormatters)).indent(2).group().combine(newline),
                    text(settings.endJsonObject)
            );
        } else if (ast instanceof AbstractJsonArrayAst) {
            AbstractJsonArrayAst jsonArrayAst = (AbstractJsonArrayAst) ast;
            List<PrettyFormatter> elementFormatters = jsonArrayAst.elements()
                    .map(x -> newline.combine(formatter(x, false)))
                    .toList()
                    .intersperse(text(settings.arrayValueSeparator));
            return combine(
                    text(settings.beginJsonArray),
                    combine(elementFormatters).indent(2).combine(newline).group(),
                    text(settings.endJsonArray)
            );
        } else if (ast instanceof AbstractJsonStringAst) {
            AbstractJsonStringAst jsonStringAst = (AbstractJsonStringAst) ast;
            String s = jsonStringAst.value().toString();
            return text(TextString.fromString(s).map(x -> x.withForegroundColor(settings.stringColor)));
        } else if (ast instanceof AbstractJsonNumberAst) {
            AbstractJsonNumberAst jsonNumberAst = (AbstractJsonNumberAst) ast;
            String s = jsonNumberAst.value().toString();
            return text(TextString.fromString(s).map(x -> x.withForegroundColor(settings.intColor)));
        } else if (ast instanceof AbstractJsonBooleanAst) {
            AbstractJsonBooleanAst jsonBooleanAst = (AbstractJsonBooleanAst) ast;
            String s = jsonBooleanAst.value().toString();
            return text(TextString.fromString(s).map(x -> x.withForegroundColor(settings.boolColor)));
        } else if (ast instanceof AbstractJsonNullAst) {
            AbstractJsonNullAst jsonNullAst = (AbstractJsonNullAst) ast;
            String s = jsonNullAst.value().toString();
            return text(TextString.fromString(s).map(x -> x.withForegroundColor(settings.nullColor)));
        } else {
            throw new RuntimeException("Unrecognized value " + ast);
        }
    }

    @Override
    public Key key() {
        return JsonAst.key;
    }
}
