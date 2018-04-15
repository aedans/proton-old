package io.github.aedans.proton.logic;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import fj.Unit;
import fj.data.List;
import fj.data.Option;
import fj.data.Seq;
import io.github.aedans.pfj.IO;
import io.github.aedans.proton.ast.Directory;
import io.github.aedans.proton.ui.Component;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.ui.components.AstDisplay;
import io.github.aedans.proton.util.Key;
import org.pf4j.ExtensionPoint;

import java.util.function.UnaryOperator;

public final class Proton implements Component {
    private static final List<KeyListener> keyListeners = Plugins.all(KeyListener.class);

    public static final Key none = new Key("none");

    public static List<KeyListener> keyListeners(Key key) {
        return keyListeners.append(AstDisplay.keyListeners(key)
                .map(x -> (KeyListener) (proton, keyStroke) ->
                        proton.mapFocusedDisplay(display -> x.apply(display, keyStroke))));
    }

    public final Directory directory;
    public final Seq<AstDisplay> displays;
    public final int focus;

    public Proton(Directory directory, Seq<AstDisplay> displays, int focus) {
        this.directory = directory;
        this.displays = displays;
        this.focus = focus;
    }

    @Override
    public Proton accept(KeyStroke keyStroke) {
        return keyListeners(focusDisplayType())
                .foldLeft((searchBox, keyListener) -> keyListener.apply(searchBox, keyStroke), this);
    }

    @Override
    public IO<Unit> render(TerminalPosition offset, TerminalSize size) {
        return getWidth().flatMap(width -> IO.run(() -> {
            Terminal.setCursor(new TerminalPosition(width * focus, 0)).run();
            displays.foldLeft((position, display) -> {
                display.render(position, size.withColumns(width)).runUnsafe();
                return position.withRelativeColumn(width);
            }, offset);
        }));
    }

    public Key focusDisplayType() {
        Option<AstDisplay> focusedDisplay = focusedDisplay();
        return focusedDisplay.isSome() ? focusedDisplay.some().ast.type() : none;
    }

    public Option<AstDisplay> focusedDisplay() {
        return displays.isEmpty() || focus < 0 ? Option.none() : Option.some(displays.index(focus));
    }

    public IO<Integer> getWidth() {
        return IO.run(() -> displays.isEmpty() ? 0 : Terminal.size().run().getColumns() / displays.length());
    }

    public Proton mapDirectory(UnaryOperator<Directory> fn) {
        return new Proton(fn.apply(directory), displays, focus);
    }

    public Proton mapDisplays(UnaryOperator<Seq<AstDisplay>> fn) {
        return new Proton(directory, fn.apply(displays), focus);
    }

    public Proton mapFocus(UnaryOperator<Integer> fn) {
        return new Proton(directory, displays, fn.apply(focus));
    }

    public Proton mapFocusedDisplay(UnaryOperator<AstDisplay> fn) {
        return mapDisplays(displays -> focus < 0 ? displays : displays.update(focus, fn.apply(displays.index(focus))));
    }

    public interface KeyListener extends ExtensionPoint {
        Proton apply(Proton proton, KeyStroke keyStroke);
    }
}
