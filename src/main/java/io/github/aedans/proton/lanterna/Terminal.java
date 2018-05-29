package io.github.aedans.proton.lanterna;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import io.github.aedans.proton.util.Logger;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public final class Terminal {
    private static final Logger logger = Logger.get(Terminal.class);

    public static final Screen screen = Single.fromCallable(() -> new DefaultTerminalFactory()
            .setTerminalEmulatorTitle("Proton")
            .setPreferTerminalEmulator(false)
            .createScreen()).blockingGet();

    static {
        screen.setCursorPosition(null);
    }

    private Terminal() {
    }

    public static Maybe<KeyStroke> read() {
        return Single.fromCallable(screen::readInput)
                .<Maybe<KeyStroke>>flatMap(input -> logger.log("Reading input", () -> input.getKeyType() == KeyType.EOF
                        ? Maybe.empty()
                        : Maybe.just(input)))
                .flatMapMaybe(x -> x);
    }

    public static Completable write(TextCharacter character, TerminalPosition position) {
        return Completable.fromAction(() -> screen.setCharacter(position, character));
    }

    public static Completable write(TextString textString, TerminalPosition position) {
        return Completable.fromAction(() -> {
            TerminalPosition position1 = position;
            for (TextCharacter textCharacter : textString) {
                write(textCharacter, position1).blockingAwait();
                position1 = position1.withRelativeColumn(1);
            }
        });
    }

    public static Completable write(Observable<TextString> text, TerminalPosition position) {
        return size().flatMapCompletable(size -> text
                .take(size.getRows())
                .reduce(position, ((position1, textString) -> {
                    write(textString.take(size.getColumns()), position).blockingAwait();
                    return position1.withRelativeRow(1);
                }))
                .ignoreElement());
    }

    public static Completable clear(TerminalPosition position, TerminalSize size) {
        return Completable.fromAction(() -> {
            for (int row = 0; row < size.getRows(); row++) {
                for (int column = 0; column < size.getColumns(); column++) {
                    write(
                            TextCharacter.DEFAULT_CHARACTER,
                            position.withRelativeRow(row).withRelativeColumn(column)
                    ).blockingAwait();
                }
            }
        });
    }

    public static Single<TerminalSize> size() {
        return Single.fromCallable(screen::getTerminalSize);
    }

    public static Completable resize() {
        return Completable.fromAction(screen::doResizeIfNecessary);
    }

    public static Completable refresh() {
        return Completable.fromAction(() -> {
            screen.refresh();
            screen.clear();
        }).andThen(resize());
    }

    public static Completable start() {
        return logger.log("Starting terminal", screen::startScreen);
    }

    public static Completable stop() {
        return logger.log("Stopping terminal", screen::stopScreen);
    }
}