package io.github.aedans.proton.system.ui;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.aedans.proton.logic.Proton;
import org.pf4j.Extension;

@Extension
public final class ProtonLeftKeyListener implements Proton.KeyListener {
    @Override
    public Proton apply(Proton proton, KeyStroke keyStroke) {
        if (keyStroke.equals(new KeyStroke(KeyType.ArrowLeft))) {
            Proton newProton = proton.mapFocus(focus -> focus <= 0 ? 0 : focus - 1);
            newProton.resetCursor();
            return newProton.rerender();
        } else {
            return proton;
        }
    }
}
