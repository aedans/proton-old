package io.github.aedans.proton.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import fj.Unit;
import fj.data.Stream;
import io.github.aedans.pfj.IO;
import io.github.aedans.proton.logic.Plugins;

import java.util.function.Predicate;

import static com.googlecode.lanterna.TerminalPosition.TOP_LEFT_CORNER;
import static com.googlecode.lanterna.TextCharacter.DEFAULT_CHARACTER;

public final class Terminal {
    private Terminal() {
    }

    public static final Predicate<KeyStroke> escape = x -> x.getKeyType() == KeyType.Escape;
    public static final Predicate<KeyStroke> line = x -> x.getKeyType() == KeyType.Enter;
    public static final Predicate<KeyStroke> identifier = x -> line.test(x) ||
            x.getKeyType() == KeyType.Character && x.getCharacter() == ' ';

    public static final Screen screen = IO.run(() -> new DefaultTerminalFactory()
            .setTerminalEmulatorTitle("Proton")
            .setPreferTerminalEmulator(false)
            .createScreen()).runUnsafe();

    public static IO<KeyStroke> read() {
        return IO.run(screen::readInput).map(x -> {
            if (x.getKeyType() == KeyType.EOF) {
                Plugins.stop().runUnsafe();
                System.exit(0);
            }
            return x;
        });
    }

    public static IO<Unit> write(TextCharacter character, TerminalPosition position) {
        return IO.run(() -> screen.setCharacter(position, character));
    }

    public static IO<Unit> clear(TerminalPosition position, TerminalSize size) {
        return IO.run(() -> {
            for (int row = 0; row < size.getRows(); row++) {
                for (int column = 0; column < size.getColumns(); column++) {
                    write(
                            DEFAULT_CHARACTER,
                            position.withRelativeRow(row).withRelativeColumn(column)
                    ).run();
                }
            }
        });
    }

    public static IO<Unit> setCursor(TerminalPosition position) {
        return IO.run(() -> screen.setCursorPosition(position));
    }

    public static IO<Unit> resetCursor() {
        return IO.run(() -> screen.setCursorPosition(TOP_LEFT_CORNER));
    }

    public static IO<TerminalSize> size() {
        return screen::getTerminalSize;
    }

    public static IO<TerminalSize> resize() {
        return screen::doResizeIfNecessary;
    }

    public static IO<Unit> refresh() {
        return IO.run(() -> {
            screen.refresh();
            screen.clear();
            resize().run();
        });
    }

    public static IO<Unit> start() {
        return IO.run(screen::startScreen).flatMap(x -> refresh());
    }

    public static IO<Unit> stop() {
        return IO.run(screen::stopScreen);
    }
}
