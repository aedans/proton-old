package io.github.aedans.proton.system.search;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ui.AstRenderer;
import io.github.aedans.proton.ui.TextString;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class SearchRenderer implements AstRenderer<Search> {
    @Override
    public Stream<Seq<TextCharacter>> render(Search search, TerminalSize size) {
        Stream<Seq<TextCharacter>> matches = search.filteredSearchSpace().take(search.scroll() + size.getRows());

        Seq<TextCharacter> text = search.text().text().foldLeft1(Seq::append);

        Stream<Seq<TextCharacter>> out = matches.cons(text);

        return out.drop(search.scroll());
    }

    @Override
    public TerminalPosition cursor(Search search, TerminalSize size) {
        return search.row() == 0 ? search.text().cursor() : new TerminalPosition(0, search.cursor());
    }

    @Override
    public String entry(Search search) {
        if (search.row() == 0) {
            if (search.filteredSearchSpace().isEmpty()) {
                return TextString.toString(search.text().text());
            } else {
                return TextString.toString(search.filteredSearchSpace().head());
            }
        } else {
            return TextString.toString(search.filteredSearchSpace().index(search.row() - 1));
        }
    }

    @Override
    public Key key() {
        return Search.key;
    }
}
