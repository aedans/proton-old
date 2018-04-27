package io.github.aedans.proton.system.proton;

import fj.data.Seq;
import io.github.aedans.pfj.IO;
import org.pf4j.Extension;

@Extension
public final class CloseAll implements Command {
    @Override
    public String command() {
        return "closeall";
    }

    @Override
    public IO<Proton> apply(Proton proton) {
        return IO.pure(proton
                .mapEditors(editors -> Seq.empty())
                .mapSelected(focus -> -1));
    }
}
