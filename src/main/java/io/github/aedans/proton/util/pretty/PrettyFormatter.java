package io.github.aedans.proton.util.pretty;

import com.googlecode.lanterna.TextCharacter;
import fj.P;
import fj.P2;
import fj.data.Seq;
import fj.data.Stream;

import static com.googlecode.lanterna.TextCharacter.DEFAULT_CHARACTER;

public interface PrettyFormatter {
    PrettyFormatterResult format(int width, boolean fit, int space, int position, int indent);

    default Stream<Seq<TextCharacter>> format(int width) {
        PrettyFormatterResult result = format(width, true, width, 0, 0);
        P2<Stream<Seq<TextCharacter>>, Seq<TextCharacter>> state = result.result().apply(P.p(Stream.nil(), Seq.empty()));
        return state._1().snoc(state._2());
    }

    PrettyFormatter empty = (width, fit, space, position, indent) -> PrettyFormatterResult.of(space, position, x -> x);

    PrettyFormatter newline = (width, fit, space, position, indent) -> {
        Seq<TextCharacter> indentChars = Seq.iterableSeq(Stream.range(0, indent).map(x -> DEFAULT_CHARACTER));
        return PrettyFormatterResult.of(
                width - indent,
                position + 1,
                string -> P.p(string._1().snoc(string._2()), indentChars)
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
                string -> string.map2(line -> line.append(text))
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
                    string -> resultB.result().compose(resultA.result()).apply(string)
            );
        };
    }

    @SuppressWarnings("Convert2MethodRef")
    static PrettyFormatter combine(Iterable<PrettyFormatter> prettyFormatters) {
        return Seq.iterableSeq(prettyFormatters).foldLeft((a, b) -> a.combine(b), empty);
    }

    static PrettyFormatter combine(PrettyFormatter... prettyFormatters) {
        return combine(Seq.arraySeq(prettyFormatters));
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
