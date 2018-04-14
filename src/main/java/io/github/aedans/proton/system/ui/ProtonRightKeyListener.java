package io.github.aedans.proton.system.ui;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.logic.Proton;
import org.pf4j.Extension;

@Extension
public final class ProtonRightKeyListener implements Proton.KeyListener {
    @Override
    public Proton apply(Proton proton, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.ArrowRight))) {
            Proton newProton = proton.mapFocus(focus -> focus >= proton.displays.length() - 1 ? proton.displays.length() - 1 : focus + 1);
            newProton.resetCursor();
            return newProton.rerender();
        } else {
            return proton;
        }
    }
}
