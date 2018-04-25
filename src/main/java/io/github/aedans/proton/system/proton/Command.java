package io.github.aedans.proton.system.proton;

import fj.data.Option;
import io.github.aedans.pfj.IO;
import io.github.aedans.proton.util.Key;
import org.pf4j.ExtensionPoint;

public interface Command extends ExtensionPoint {
    String command();

    default Option<Key> type() {
        return Option.none();
    }

    IO<Proton> apply(Proton proton);
}
