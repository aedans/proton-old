package io.github.aedans.proton.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import io.github.aedans.proton.logic.Plugins;
import io.github.aedans.proton.util.ExceptionUtils;

import java.util.function.Predicate;

import static io.github.aedans.proton.util.ExceptionUtils.doUnchecked;

public final class Terminal {
    private Terminal() {
    }

    public static final Predicate<KeyStroke> escape = x -> x.getKeyType() == KeyType.Escape;
    public static final Predicate<KeyStroke> line = x -> x.getKeyType() == KeyType.Enter;
    public static final Predicate<KeyStroke> identifier = x -> line.test(x) ||
            x.getKeyType() == KeyType.Character && x.getCharacter() == ' ';

    public static final Screen screen = doUnchecked(() -> new DefaultTerminalFactory()
            .setTerminalEmulatorTitle("Proton")
            .setPreferTerminalEmulator(false)
            .createScreen());

    public static KeyStroke read() {
        KeyStroke keyStroke = doUnchecked(screen::readInput);
        if (keyStroke.getKeyType() == KeyType.EOF) {
            Plugins.stop();
            System.exit(0);
        }
        return keyStroke;
    }

    public static void write(TextCharacter character, TerminalPosition position) {
        screen.setCharacter(position, character);
    }

    public static void clear(TerminalPosition position, TerminalSize size) {
        for (int row = 0; row < size.getRows(); row++) {
            for (int column = 0; column < size.getColumns(); column++) {
                write(TextCharacter.DEFAULT_CHARACTER, position.withRelativeRow(row).withRelativeColumn(column));
            }
        }
    }

    public static void setCursor(TerminalPosition position) {
        screen.setCursorPosition(position);
    }

    public static void resetCursor() {
        screen.setCursorPosition(TerminalPosition.TOP_LEFT_CORNER);
    }

    public static TerminalSize size() {
        return screen.getTerminalSize();
    }

    public static void resize() {
        screen.doResizeIfNecessary();
    }

    public static void refresh() {
        doUnchecked((ExceptionUtils.CheckedRunnable) screen::refresh);
        screen.clear();
        resize();
    }

    public static void start() {
        doUnchecked(screen::startScreen);
        refresh();
    }

    public static void stop() {
        doUnchecked(screen::stopScreen);
    }
}
