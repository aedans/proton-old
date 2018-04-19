package io.github.aedans.proton.system.proton;

import com.googlecode.lanterna.TerminalSize;
import fj.data.Option;
import fj.data.Seq;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.system.directory.Directory;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.util.Key;

import java.util.function.UnaryOperator;

public final class Proton implements Ast {
    public static final Key key = Key.unique("proton");
    public static final Key none = Key.unique("none");

    public final Directory directory;
    public final Seq<Editor> editors;
    public final int focus;

    public Proton(Directory directory, Seq<Editor> editors, int focus) {
        this.directory = directory;
        this.editors = editors;
        this.focus = focus;
    }

    @Override
    public Key type() {
        return key;
    }

    public Option<Editor> getFocusedEditor() {
        if (editors.isEmpty()) {
            return Option.none();
        } else {
            return Option.some(editors.index(focus));
        }
    }

    public Key getFocusedEditorType() {
        return getFocusedEditor().map(x -> x.ast.type()).orSome(() -> none);
    }

    public int getEditorWidth(TerminalSize size) {
        return editors.isEmpty() ? 0 : size.getColumns() / editors.length();
    }

    public Proton mapDirectory(UnaryOperator<Directory> fn) {
        return new Proton(fn.apply(directory), editors, focus);
    }

    public Proton mapEditors(UnaryOperator<Seq<Editor>> fn) {
        return new Proton(directory, fn.apply(editors), focus);
    }

    public Proton mapFocus(UnaryOperator<Integer> fn) {
        return new Proton(directory, editors, fn.apply(focus));
    }

    public Proton withDirectory(Directory directory) {
        return mapDirectory(x -> directory);
    }

    public Proton withEditors(Seq<Editor> editors) {
        return mapEditors(x -> editors);
    }

    public Proton withFocus(int focus) {
        return mapFocus(x -> focus);
    }
}
