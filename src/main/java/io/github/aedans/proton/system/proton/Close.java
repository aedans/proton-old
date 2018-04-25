package io.github.aedans.proton.system.proton;

import io.github.aedans.pfj.IO;
import io.github.aedans.proton.system.proton.Command;
import io.github.aedans.proton.system.proton.Proton;
import io.github.aedans.proton.util.Key;
import org.pf4j.Extension;

@Extension
public final class Close implements Command {
    @Override
    public String command() {
        return "close";
    }

    @Override
    public IO<Proton> apply(Proton proton) {
        return IO.pure(proton
                .mapEditors(editors -> editors.delete(proton.selected()))
                .mapSelected(focus -> focus - 1));
    }
}
