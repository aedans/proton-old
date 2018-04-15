package io.github.aedans.proton.logic;

import io.github.aedans.pfj.IO;
import io.github.aedans.proton.util.Key;
import org.pf4j.ExtensionPoint;

public interface Command extends ExtensionPoint {
    String command();

    Key type();

    IO<Proton> apply(Proton proton);
}
