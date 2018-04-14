package io.github.aedans.proton.system.text;

import com.googlecode.lanterna.TerminalPosition;
import fj.data.Seq;
import io.github.aedans.proton.logic.Command;
import io.github.aedans.proton.logic.Proton;
import io.github.aedans.proton.ui.Request;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.ui.components.TextBox;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class TextInsertCommand implements Command {
    @Override
    public String command() {
        return "i";
    }

    @Override
    public Key type() {
        return TextAst.key;
    }

    @Override
    public Proton apply(Proton proton) {
        String request = new Request()
                .withComponent(new TextBox()
                        .withText(((TextAst) proton.focusedDisplay().some().ast).text.map(TextString::fromString))
                        .withCursor(TerminalPosition.TOP_LEFT_CORNER))
                .run();
        Seq<String> text = Seq.arraySeq(request.split("\n"));
        TextAst ast = new TextAst(text);
        proton.resetCursor();
        return proton
                .mapFocusedDisplay(display -> display.mapAst(x -> ast))
                .mapDirectory(directory -> directory.put(proton.focusedDisplay().some().path, () -> ast));
    }
}
