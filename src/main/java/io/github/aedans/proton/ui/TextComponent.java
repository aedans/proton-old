package io.github.aedans.proton.ui;

import com.googlecode.lanterna.input.KeyStroke;

public interface TextComponent extends Component {
    String text();

    @Override
    TextComponent accept(KeyStroke keyStroke);
}
