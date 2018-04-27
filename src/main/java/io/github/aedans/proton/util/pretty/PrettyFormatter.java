package io.github.aedans.proton.util.pretty;

import com.googlecode.lanterna.TextCharacter;
import fj.data.Seq;
import fj.data.Stream;

import static com.googlecode.lanterna.TextCharacter.DEFAULT_CHARACTER;

public interface PrettyFormatter {
    PrettyFormatterResult format(int width, boolean fit, int space, int position, int indent);

    default Stream<Seq<TextCharacter>> format(int width) {
        PrettyFormatterResult result = format(width, true, width, 0, 0);
        PrettyFormatterResultState state = result.result().apply(PrettyFormatterResultState.of(Stream.nil(), Seq.empty()));
        return state.all().reverse();
    }

    PrettyFormatter empty = (width, fit, space, position, indent) -> PrettyFormatterResult.of(space, position, x -> x);

    PrettyFormatter newline = (width, fit, space, position, indent) -> {
        Seq<TextCharacter> indentChars = Seq.iterableSeq(Stream.range(0, indent).map(x -> DEFAULT_CHARACTER));
        return PrettyFormatterResult.of(
                width - indent,
                position + 1,
                state -> PrettyFormatterResultState.of(state.all(), indentChars)
        );
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
        return (width, fit, space, position, indent) -> PrettyFormatterResult.of(
                space - length,
                position + length,
                state -> state.withLine(state.line().append(text))
        );
    }

    static PrettyFormatter text(TextCharacter character) {
        return text(Seq.single(character));
    }

    default PrettyFormatter combine(PrettyFormatter b) {
        return (width, fit, space, position, indent) -> {
            PrettyFormatterResult resultA = format(width, fit, space, position, indent);
            PrettyFormatterResult resultB = b.format(width, fit, resultA.space(), resultA.position(), indent);
            return PrettyFormatterResult.of(
                    resultB.space(),
                    resultB.position(),
                    state -> resultB.result().compose(resultA.result()).apply(state)
            );
        };
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
}
