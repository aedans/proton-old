package io.github.aedans.proton.ui;

import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import io.github.aedans.proton.ast.Ast;
import io.github.aedans.proton.util.Unique;
import org.pf4j.ExtensionPoint;

public interface AstRenderer extends ExtensionPoint, Unique {
    Seq<Seq<TextCharacter>> render(Ast ast);
}
