package io.github.aedans.proton.system.proton;

import com.googlecode.lanterna.TerminalSize;
import fj.data.Option;
import fj.data.Seq;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.system.directory.Directory;
import io.github.aedans.proton.ui.Editor;
import io.github.aedans.proton.ui.Terminal;
import io.github.aedans.proton.util.AbstractImmutable;
import io.github.aedans.proton.util.Key;
import org.immutables.value.Value;

import java.util.function.UnaryOperator;

@Value.Immutable
@AbstractImmutable
public abstract class AbstractProton implements Ast {
    public static final Key key = Key.of(Proton.class);

    @Value.Parameter
    public abstract Directory directory();

    @Value.Default
    public Seq<Editor> editors() {
        return Seq.empty();
    }

    @Value.Default
    public int selected() {
        return -1;
    }

    @Value.Default
    public boolean focused() {
        return false;
    }

    @Value.Default
    public TerminalSize size() {
        return Terminal.size().runUnsafe();
    }

    @Value.Check
    public AbstractProton normalize() {
        if (editors().length() > 0 && selected() < 0) {
            return mapSelected(x -> 0);
        } else if (selected() > editors().length() - 1) {
            return mapSelected(x -> editors().length() - 1);
        } else {
            TerminalSize realSize = size().withColumns(editorWidth(size()));
            if (editors().toStream().find(x -> !x.size().equals(realSize)).isSome()) {
                return mapEditors(editors -> editors.map(editor -> editor.withSize(realSize)));
            } else {
                return this;
            }
        }
    }

    public Option<Integer> focusedEditorIndex() {
        return !focused() || editors().isEmpty() ? Option.none() : Option.some(selected());
    }

    public Option<Editor> focusedEditor() {
        return focusedEditorIndex().map(editors()::index);
    }

    public Option<Key> focusedEditorType() {
        return focusedEditor().map(x -> x.ast().type());
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
