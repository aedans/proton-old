package io.github.aedans.proton.system.proton;

import com.googlecode.lanterna.TerminalSize;
import fj.data.Option;
import fj.data.Seq;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.system.directory.Directory;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.util.AbstractImmutable;
import io.github.aedans.proton.util.Key;
import org.immutables.value.Value;

import java.util.function.UnaryOperator;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractProton implements Ast {
    public static final Key key = Key.unique("proton");
    public static final Key none = Key.unique("none");

    @Value.Parameter
    public abstract Directory directory();

    @Value.Parameter
    public abstract Seq<Editor> editors();

    @Value.Parameter
    public abstract int selected();

    @Value.Parameter
    public abstract boolean focused();

    public Proton normalize(TerminalSize size) {
        if (selected() < 0) {
            return mapSelected(x -> 0).normalize(size);
        } else if (selected() > editors().length() - 1) {
            return mapSelected(x -> editors().length() - 1);
        } else {
            TerminalSize realSize = size.withColumns(editorWidth(size));
            return mapEditors(editors ->
                    editors.map(editor -> editor.withSize(realSize)));
        }
    }

    public Option<Integer> focusedEditorIndex() {
        return !focused() || editors().isEmpty() ? Option.none() : Option.some(selected());
    }

    public Option<Editor> focusedEditor() {
        return focusedEditorIndex().map(editors()::index);
    }

    public Key focusedEditorType() {
        return focusedEditor().map(x -> x.ast().type()).orSome(() -> none);
    }

    public int editorWidth(TerminalSize size) {
        return editors().isEmpty() ? 0 : size.getColumns() / editors().length();
    }

    public Proton mapDirectory(UnaryOperator<Directory> fn) {
        return Proton.copyOf(this).withDirectory(fn.apply(directory()));
    }

    public Proton mapEditors(UnaryOperator<Seq<Editor>> fn) {
        return Proton.copyOf(this).withEditors(fn.apply(editors()));
    }

    public Proton mapSelected(UnaryOperator<Integer> fn) {
        return Proton.copyOf(this).withSelected(fn.apply(selected()));
    }

    public Proton mapFocused(UnaryOperator<Boolean> fn) {
        return Proton.copyOf(this).withFocused(fn.apply(focused()));
    }

    @Override
    public Key type() {
        return key;
    }
}
