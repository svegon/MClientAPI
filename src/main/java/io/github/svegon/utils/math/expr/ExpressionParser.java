package io.github.svegon.utils.math.expr;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.ParseException;

@TestOnly
public final class ExpressionParser {
    private ExpressionParser() {
        throw new UnsupportedOperationException();
    }

    public static BigDecimal fastEval(final @NotNull Reader reader, MathContext mc,
                                      DecimalFormat format) throws IOException {
        EvaluationStack stack = new EvaluationStack(mc);
        StringBuilder phraseBuffer = new StringBuilder();
        int codePoint;

        format.setParseBigDecimal(true);

        while ((codePoint = reader.read()) >= 0) {
            char chr = (char) codePoint;

            if (chr == ' ') {
                continue;
            }

            if (chr == '(' || chr == '[' || chr == '{') {
                emptyNumberBuffer(format, stack, phraseBuffer);

                if (stack.peek().operation() == EvaluationStack.Operation.NONE) {
                    stack.push(EvaluationStack.Operation.MUL);
                }

                stack.push();
                continue;
            }

            if (chr == ')' || chr == ']' || chr == '}') {
                emptyNumberBuffer(format, stack, phraseBuffer);
                stack.pop();

                if (stack.peek().operation() == null) {
                    stack.push(EvaluationStack.Operation.MUL);
                }

                continue;
            }

            if (chr == '+') {
                emptyNumberBuffer(format, stack, phraseBuffer);

                stack.push(EvaluationStack.Operation.SUM);
                continue;
            }

            if (chr == '-') {
                emptyNumberBuffer(format, stack, phraseBuffer);

                stack.push(EvaluationStack.Operation.SUB);
                continue;
            }

            if (chr == '*') {
                emptyNumberBuffer(format, stack, phraseBuffer);

                if ((chr = (char) reader.read()) == '*') {
                    stack.push(EvaluationStack.Operation.POW);
                } else {
                    phraseBuffer.append(chr);
                    stack.push(EvaluationStack.Operation.MUL);
                }

                continue;
            }

            if (chr == '/') {
                emptyNumberBuffer(format, stack, phraseBuffer);

                if ((chr = (char) reader.read()) == '/') {
                    stack.push(EvaluationStack.Operation.FLOOR_DIV);
                } else {
                    phraseBuffer.append(chr);
                    stack.push(EvaluationStack.Operation.DIV);
                }

                continue;
            }

            if (chr == '%') {
                emptyNumberBuffer(format, stack, phraseBuffer);

                stack.push(EvaluationStack.Operation.MOD);
                continue;
            }

            phraseBuffer.append(chr);
        }

        return stack.eval();
    }

    public static BigDecimal fastEval(final @NotNull Reader reader, MathContext mc) throws IOException {
        return fastEval(reader, mc, new DecimalFormat());
    }

    public static BigDecimal fastEval(final @NotNull Reader reader) throws IOException {
        return fastEval(reader, MathContext.UNLIMITED);
    }

    private static void emptyNumberBuffer(DecimalFormat numberParser, EvaluationStack stack, StringBuilder buffer) {
        if (!buffer.isEmpty()) {
            stack.push(parseBigDecimal(buffer, numberParser));
            buffer.setLength(0);
        }
    }

    private static BigDecimal parseBigDecimal(CharSequence input, DecimalFormat format) {
        try {
            return (BigDecimal) format.parse(input.toString());
        } catch (ParseException e) {
            throw new IllegalEvaluationException(e);
        }
    }
}
