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
    public final int selected;
    public final boolean focused;

    public Proton(Directory directory, Seq<Editor> editors, int selected, boolean focused) {
        this.directory = directory;
        this.editors = editors;
        this.selected = selected;
        this.focused = focused;
    }

    @Override
    public Key type() {
        return key;
    }

    public Proton normalize() {
        if (selected < 0)
            return withSelected(0).normalize();
        else if (selected > editors.length() - 1)
            return withSelected(editors.length() - 1);
        else
            return this;
    }

    public Option<Integer> getFocusedEditorIndex() {
        return !focused || editors.isEmpty() ? Option.none() : Option.some(selected);
    }

    public Option<Editor> getFocusedEditor() {
        return getFocusedEditorIndex().map(editors::index);
    }

    public Key getFocusedEditorType() {
        return getFocusedEditor().map(x -> x.ast.type()).orSome(() -> none);
    }

    public int getEditorWidth(TerminalSize size) {
        return editors.isEmpty() ? 0 : size.getColumns() / editors.length();
    }

    public Proton mapDirectory(UnaryOperator<Directory> fn) {
        return new Proton(fn.apply(directory), editors, selected, focused);
    }

    public Proton mapEditors(UnaryOperator<Seq<Editor>> fn) {
        return new Proton(directory, fn.apply(editors), selected, focused);
    }

    public Proton mapSelected(UnaryOperator<Integer> fn) {
        return new Proton(directory, editors, fn.apply(selected), focused);
    }

    public Proton mapFocused(UnaryOperator<Boolean> fn) {
        return new Proton(directory, editors, selected, fn.apply(focused));
    }

    public Proton withDirectory(Directory directory) {
        return mapDirectory(x -> directory);
    }

    public Proton withEditors(Seq<Editor> editors) {
        return mapEditors(x -> editors);
    }

    public Proton withSelected(int selected) {
        return mapSelected(x -> selected);
    }

    public Proton withFocused(boolean focused) {
        return mapFocused(x -> focused);
    }
}
