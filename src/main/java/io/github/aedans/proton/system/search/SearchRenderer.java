package io.github.aedans.proton.system.search;

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
        Stream<Seq<TextCharacter>> matches = search.searchSpace
                .filter(x -> search.filter.apply(TextString.toString(x), TextString.toString(search.search.text.toStream())));

        Seq<TextCharacter> text = search.search.text.toStream().foldLeft1(Seq::append);

        return matches.cons(text);
    }

    @Override
    public String text(Search search) {
        return TextString.toString(search.search.text.toStream());
    }

    @Override
    public Key key() {
        return Search.key;
    }
}