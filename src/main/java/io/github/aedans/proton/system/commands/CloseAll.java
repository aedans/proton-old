package io.github.aedans.proton.system.commands;

import fj.data.Seq;
import io.github.aedans.proton.logic.Command;
import io.github.aedans.proton.logic.Proton;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class CloseAll implements Command {
    @Override
    public String command() {
        return "closeall";
    }

    @Override
    public Key type() {
        return Proton.none;
    }

    @Override
    public Proton apply(Proton proton) {
        return proton
                .mapDisplays(displays -> Seq.empty())
                .mapFocus(focus -> -1);
    }
}
