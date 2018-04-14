package io.github.aedans.proton.system.commands;

import io.github.aedans.proton.logic.Command;
import io.github.aedans.proton.logic.Proton;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class Close implements Command {
    @Override
    public String command() {
        return "close";
    }

    @Override
    public Key type() {
        return Proton.none;
    }

    @Override
    public Proton apply(Proton proton) {
        return proton
                .mapDisplays(displays -> displays.delete(proton.focus))
                .mapFocus(focus -> focus - 1);
    }
}
