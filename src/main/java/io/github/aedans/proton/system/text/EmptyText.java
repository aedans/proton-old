package io.github.aedans.proton.system.text;

import io.github.aedans.proton.system.proton.New;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class EmptyText implements New.Empty<Text> {
    @Override
    public Text create() {
        return Text.builder().build();
    }

    @Override
    public Key key() {
        return Text.key;
    }
}
