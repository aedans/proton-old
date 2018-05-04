package io.github.aedans.proton.util.pretty;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;
import io.github.aedans.proton.ui.AstRendererResult;

import static com.googlecode.lanterna.TextCharacter.DEFAULT_CHARACTER;

public interface PrettyFormatter {
    PrettyFormatter empty = (width, fit, space, position, indent) -> PrettyFormatterResult.builder()
            .space(space)
            .position(position)
            .result(x -> x)
            .build();
    PrettyFormatter newline = (width, fit, space, position, indent) -> {
        Seq<TextCharacter> indentChars = Seq.iterableSeq(Stream.range(0, indent).map(x -> DEFAULT_CHARACTER));
        return PrettyFormatterResult.builder()
                .space(width - indent)
                .position(position + 1)
                .result(state -> state.withText(state.text().cons(indentChars)))
                .build();
    };
    PrettyFormatter linebreak = (width, fit, space, position, indent) -> {
        if (fit) {
            return empty.format(width, true, space, position, indent);
        } else {
            return newline.format(width, false, space, position, indent);
        }
    };

    static PrettyFormatter text(Seq<TextCharacter> text) {
        int length = text.length();
        return (width, fit, space, position, indent) -> PrettyFormatterResult.builder()
                .space(space - length)
                .position(position + length)
                .result(state -> state.withText(state.text().tail().f().cons(state.text().head().append(text))))
                .build();
    }

    static PrettyFormatter text(TextCharacter character) {
        return text(Seq.single(character));
    }

    @SuppressWarnings("Convert2MethodRef")
    static PrettyFormatter combine(Stream<PrettyFormatter> prettyFormatters) {
        return prettyFormatters.foldLeft((a, b) -> a.combine(b), empty);
    }

    static PrettyFormatter combine(Iterable<PrettyFormatter> prettyFormatters) {
        return combine(Stream.iterableStream(prettyFormatters));
    }

    static PrettyFormatter combine(PrettyFormatter... prettyFormatters) {
        return combine(Stream.arrayStream(prettyFormatters));
    }

    PrettyFormatterResult format(int width, boolean fit, int space, int position, int indent);

    default AstRendererResult format(int width) {
        PrettyFormatterResult result = format(width, true, width, 0, 0);
        AstRendererResult state = result.result().apply(AstRendererResult.builder().text(Stream.single(Seq.empty())).build());
        return state.withText(state.text().reverse());
    }

    default PrettyFormatter combine(PrettyFormatter b) {
        return (width, fit, space, position, indent) -> {
            PrettyFormatterResult resultA = format(width, fit, space, position, indent);
            PrettyFormatterResult resultB = b.format(width, fit, resultA.space(), resultA.position(), indent);
            return PrettyFormatterResult.builder()
                    .space(resultB.space())
                    .position(resultB.position())
                    .result(state -> resultB.result().compose(resultA.result()).apply(state))
                    .build();
        };
    }

    default PrettyFormatter group() {
        return (width, fit, space, position, indent) -> {
            int endPoint = position + space;
            PrettyFormatterResult format = format(width, true, space, position, indent);
            if (format.position() <= endPoint)
                return format;
            else
                return format(width, false, space, position, indent);
        };
    }

    default PrettyFormatter indent(int amount) {
        return (width, fit, space, position, indent) -> format(width, fit, space, position, indent + amount);
    }

    default PrettyFormatter withCursor() {
        return (width, fit, space, position, indent) -> {
            PrettyFormatterResult result = format(width, fit, space, position, indent);
            return result.withResult(state -> {
                TerminalPosition cursor = new TerminalPosition(width - result.space(), state.text().length());
                return result.result().apply(state.withCursor(cursor));
            });
        };
    }
}
