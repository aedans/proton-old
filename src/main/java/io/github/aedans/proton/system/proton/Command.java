package io.github.aedans.proton.system.proton;

import fj.data.Option;
import io.github.aedans.pfj.IO;
import io.github.aedans.proton.util.Key;
import org.pf4j.ExtensionPoint;

public interface Command extends ExtensionPoint, Comparable<Command> {
    String command();

    IO<Proton> apply(Proton proton);

    default Option<Key> type() {
        return Option.none();
    }

    @Override
    default int compareTo(Command o) {
        return command().compareTo(o.command());
    }
}
