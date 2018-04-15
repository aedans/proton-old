package io.github.aedans.proton.ui.components;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import fj.Unit;
import fj.data.List;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.pfj.IO;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.logic.Plugins;
import io.github.aedans.proton.ui.*;
import io.github.aedans.proton.util.Key;
import org.pf4j.ExtensionPoint;

import java.util.function.UnaryOperator;

public final class AstDisplay implements TextComponent {
    private static final List<KeyListener> keyListeners = Plugins.all(KeyListener.class);

    public static List<KeyListener> keyListeners(Key key) {
        return keyListeners.filter(x -> x.type().equals(key));
    }

    public final Ast ast;
    public final AstRenderer renderer;
    public final Seq<String> path;

    public AstDisplay(Ast ast, AstRenderer renderer, Seq<String> path) {
        this.ast = ast;
        this.renderer = renderer;
        this.path = path;
    }

    @Override
    public AstDisplay accept(KeyStroke keyStroke) {
        return keyListeners(ast.type()).foldLeft((display, keyListener) -> keyListener.apply(display, keyStroke), this);
    }

    @Override
    public IO<Unit> render(TerminalPosition offset, TerminalSize size) {
        return IO.run(() -> {
            Stream<Seq<TextCharacter>> text = renderer.render(ast).toStream();

            TerminalSize realSize = new TerminalSize(
                    Math.min(text.foldLeft((max, line) -> Math.max(max, line.length()), 0), size.getColumns()),
                    Math.min(text.length(), size.getRows())
            );

            Terminal.clear(offset, realSize).run();

            TextString.render(text, offset).run();
        });
    }

    @Override
    public String text() {
        return TextString.toString(renderer.render(ast).toStream());
    }

    public AstDisplay mapAst(UnaryOperator<Ast> fn) {
        return new AstDisplay(fn.apply(ast), renderer, path);
    }

    public AstDisplay mapDisplay(UnaryOperator<AstRenderer> fn) {
        return new AstDisplay(ast, fn.apply(renderer), path);
    }

    public AstDisplay mapPath(UnaryOperator<Seq<String>> fn) {
        return new AstDisplay(ast, renderer, fn.apply(path));
    }

    public interface KeyListener extends ExtensionPoint {
        Key type();

        AstDisplay apply(AstDisplay display, KeyStroke keyStroke);
    }
}
