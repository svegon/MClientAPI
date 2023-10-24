package com.github.svegon.utils.string;

import com.github.svegon.utils.ConditionUtil;
import com.github.svegon.utils.collections.collecting.CollectingUtil;
import com.github.svegon.utils.collections.stream.StreamUtil;
import com.github.svegon.utils.math.MathUtil;
import com.github.svegon.utils.math.really_big_math.InfiniFloat;
import com.github.svegon.utils.math.really_big_math.InfiniMathUtil;
import com.github.svegon.utils.math.really_big_math.InfiniNumber;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.chars.CharList;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public final class StringUtil {
    private StringUtil() {
        throw new AssertionError();
    }

    public static final String NaN = "NaN";
    public static final String POSITIVE_INFINITY = "\u221e";
    public static final String NEGATIVE_INFINITY = "-" + POSITIVE_INFINITY;

    public static final List<String> DECIMAL_DIGITS = ImmutableList.of("0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9");
    public static final String DEFAULT_POINTER = ".";
    public static final Pair<String, String> STANDARD_SIGNS = Pair.of("", "-");
    public static final CharList DECIMAL_DIGIT_CHARS = StreamUtil.mapToChar(DECIMAL_DIGITS.stream(),
            (s) -> s.charAt(0)).collect(CollectingUtil.toImmutableCharList());

    public static final ImmutableMap<Pattern, BiFunction<String, Object, String>> STANDARD_STRICT_FORMAT;

    public static String toString(double d, List<String> digitSet, String pointer, Pair<String, String> signs) {
        if (Double.isNaN(d)) {
            return NaN;
        }

        if (d == Double.NEGATIVE_INFINITY) {
            return NEGATIVE_INFINITY;
        }

        if (d == Double.POSITIVE_INFINITY) {
            return POSITIVE_INFINITY;
        }

        StringBuilder builder = new StringBuilder();

        if (Double.compare(d, 0) < 0) {
            d = -d;
            builder.append(signs.right());
        } else {
            builder.append(signs.left());
        }

        int base = digitSet.size();
        double intPart = Math.floor(d);
        double floatPart = d - intPart;

        if (intPart == 0) {
            builder.append(digitSet.get(0));
        } else {
            while (intPart != 0) {
                builder.insert(0, digitSet.get((int) Math.floor(intPart % base)));
                intPart = MathUtil.floorDiv(intPart, base);
            }
        }

        if (floatPart == 0) {
            return builder.toString();
        }

        builder.append(pointer);

        while (floatPart != 0) {
            floatPart *= base;
            int integerPart = (int) floatPart;
            builder.append(digitSet.get(integerPart));
            floatPart = floatPart - intPart;
        }

        return builder.toString();
    }

    public static String toString(@NotNull InfiniNumber n, List<String> digitSet, String pointer, Pair<String,
            String> signs) {
        StringBuilder builder = new StringBuilder();

        if (n.compareTo(0) < 0) {
            n = n.neg();
            builder.append(signs.right());
        } else {
            builder.append(signs.left());
        }

        int base = digitSet.size();
        InfiniNumber intPart = n.floor();
        InfiniNumber floatPart = n.substract(intPart);
        Pair<? extends InfiniNumber, ? extends InfiniNumber> charAndRemainder;

        if (intPart.compareTo(0) == 0) {
            builder.append(digitSet.get(0));
        } else {
            while (intPart.compareTo(0) != 0) {
                charAndRemainder = intPart.divMod(base);
                builder.insert(0, digitSet.get(charAndRemainder.right().intValue()));
                intPart = charAndRemainder.left();
            }
        }

        if (floatPart.compareTo(0) == 0) {
            return builder.toString();
        }

        builder.append(pointer);

        while (floatPart.compareTo(0) != 0) {
            charAndRemainder = floatPart.mul(base).divMod(1);
            builder.append(digitSet.get(charAndRemainder.left().intValue()));
            floatPart = charAndRemainder.right();
        }

        return builder.toString();
    }

    public static String toString(InfiniNumber n, List<String> charSet, String pointer) {
        return toString(n, charSet, pointer, STANDARD_SIGNS);
    }

    public static String toString(InfiniNumber n, List<String> charSet) {
        return toString(n, charSet, DEFAULT_POINTER);
    }

    public static String toString(InfiniNumber n) {
        return toString(n, DECIMAL_DIGITS);
    }

    public static InfiniNumber parseInfiniNumber(String s, CharList charSet, String pointer, Pair<String,
            String> signs) {
        if (s.equals(NaN)) {
            return InfiniMathUtil.NaN;
        }

        if (s.equals(POSITIVE_INFINITY) || s.equals(String.valueOf(Double.POSITIVE_INFINITY))) {
            return InfiniMathUtil.POSITIVE_INFINITY;
        }

        if (s.equals(NEGATIVE_INFINITY) || s.equals(String.valueOf(Double.NEGATIVE_INFINITY))) {
            return InfiniMathUtil.NEGATIVE_INFINITY;
        }

        boolean sign;

        if (s.startsWith(signs.first())) {
            sign = false;
            s = s.substring(signs.first().length());
        } else if (s.startsWith(signs.right())) {
            sign = true;
            s = s.substring(signs.right().length());
        } else {
            throw new NumberFormatException("invalid signum");
        }

        String[] var = s.split(pointer);

        switch (var.length) {
            case 0 -> {
                return InfiniFloat.ZERO;
            }

            case 1 -> {
                InfiniFloat base = InfiniFloat.valueOf(charSet.size());
                int length = s.length();
                InfiniNumber result = InfiniFloat.ZERO;

                for (int i = 0; i < length; i++) {
                    int digit = charSet.indexOf(s.charAt(i));

                    if (digit < 0) {
                        throw new NumberFormatException();
                    }

                    result = result.add(InfiniFloat.valueOf(digit)
                            .mul(base.pow(InfiniFloat.valueOf(length - i - 1))));
                    // invert is a programming shortcut for -1-i
                }

                return sign ? result.neg() : result;
            }

            case 2 -> {
                InfiniNumber result = InfiniFloat.ZERO;
                int length = s.length();
                InfiniFloat base = InfiniFloat.valueOf(charSet.size());
                s = var[1];

                for (int i = 0; i < length; i++) {
                    int digit = charSet.indexOf(s.charAt(i));

                    if (digit < 0) {
                        throw new NumberFormatException();
                    }

                    result = result.add(InfiniFloat.valueOf(digit)
                            .mul(base.pow(InfiniFloat.valueOf(~i)))); // invert is a programming shortcut for -1-i
                }

                result = result.add(parseInfiniNumber(var[0], charSet, pointer, signs));

                if (sign) {
                    result = result.neg();
                }

                return result;
            }

            default -> throw new NumberFormatException();
        }
    }

    public static InfiniNumber parseInfiniNumber(String s, CharList charSet, String pointer) {
        return parseInfiniNumber(s, charSet, pointer, STANDARD_SIGNS);
    }

    public static InfiniNumber parseInfiniNumber(String s, CharList charSet) {
        return parseInfiniNumber(s, charSet, DEFAULT_POINTER);
    }

    public static InfiniNumber parseInfiniNumber(String s) {
        return parseInfiniNumber(s, DECIMAL_DIGIT_CHARS);
    }

    public static StringAsList asList(final String string, final int startingIndex, final int endingIndex) {
        Preconditions.checkPositionIndexes(startingIndex, endingIndex, string.length());
        return new StringAsList(string.substring(startingIndex, endingIndex));
    }

    public static StringAsList asList(final String string, final int startingIndex) {
        return asList(string, startingIndex, string.length());
    }

    public static StringAsList asList(final String string) {
        return new StringAsList(Preconditions.checkNotNull(string));
    }

    public static String strictFormat(final @NotNull Map<Pattern, BiFunction<String, Object, String>>
                                              unformatted2replacement, final @NotNull String str,
                                      final Iterator<Object> args) {
        Iterator<Pair<MatchResult, BiFunction<String, Object, String>>> itr =
                unformatted2replacement.entrySet().parallelStream().flatMap(e -> e.getKey().matcher(str).results()
                                .map(r -> Pair.of(r, e.getValue())))
                        .sorted(Comparator.comparingInt(p -> p.first().start())).iterator();
        StringBuilder builder = new StringBuilder(str.length());
        int nextIndex = 0;

        while (itr.hasNext() && args.hasNext()) {
            Pair<MatchResult, BiFunction<String, Object, String>> match = itr.next();
            MatchResult result = match.first();

            if (result.start() <= nextIndex) {
                continue;
            }

            if (result.start() > 0 && str.charAt(result.start() - 1) == str.charAt(result.start())) {
                continue;
            }

            String group = result.group();
            String replacement = match.second().apply(group, args.next());

            builder.append(str, nextIndex, result.start()).append(replacement == null ? group : replacement);

            nextIndex = result.end();
        }

        return builder.append(str, nextIndex, str.length()).toString();
    }

    public static String strictFormat(final @NotNull Map<Pattern, BiFunction<String, Object, String>>
                                              unformatted2replacement, final @NotNull String str,
                                      final Iterable<Object> args) {
        return strictFormat(unformatted2replacement, str, args.iterator());
    }

    public static String strictFormat(final @NotNull Map<Pattern, BiFunction<String, Object, String>>
                                              unformatted2replacement, final @NotNull String str,
                                      final Object @NotNull ... args) {
        Iterator<Pair<MatchResult, BiFunction<String, Object, String>>> itr =
                unformatted2replacement.entrySet().parallelStream().flatMap(e -> e.getKey().matcher(str).results()
                                .map(r -> Pair.of(r, e.getValue())))
                .sorted(Comparator.comparingInt(p -> p.first().start())).iterator();
        StringBuilder builder = new StringBuilder(str.length());
        int nextArg = 0;
        int nextIndex = 0;

        while (itr.hasNext() && nextArg < args.length) {
            Pair<MatchResult, BiFunction<String, Object, String>> match = itr.next();
            MatchResult result = match.first();

            if (result.start() <= nextIndex) {
                continue;
            }

            if (result.start() > 0 && str.charAt(result.start() - 1) == str.charAt(result.start())) {
                continue;
            }

            String group = result.group();
            String replacement = match.second().apply(group, args[nextArg++]);

            builder.append(str, nextIndex, result.start()).append(replacement == null ? group : replacement);

            nextIndex = result.end();
        }

        return builder.append(str, nextIndex, str.length()).toString();
    }

    public static String strictFormat(final @NotNull String str, Object @NotNull ... args) {
        return strictFormat(STANDARD_STRICT_FORMAT, str, args);
    }

    public static String strictFormat(final @NotNull String str, final Iterator<Object> args) {
        return strictFormat(STANDARD_STRICT_FORMAT, str, args);
    }

    public static String strictFormat(final @NotNull String str, final Iterable<Object> args) {
        return strictFormat(STANDARD_STRICT_FORMAT, str, args);
    }

    private static final class MatchResultUniquenessPredicate
            implements Predicate<Pair<MatchResult, Predicate<Object>>> {
        private final IntPredicate intUniqueness = ConditionUtil.intUniquenessPredicate();

        @Override
        public boolean test(Pair<MatchResult, Predicate<Object>> matchResultPredicatePair) {
            return intUniqueness.test(matchResultPredicatePair.first().start());
        }
    }

    static {
        ImmutableMap.Builder<Pattern, BiFunction<String, Object, String>> builder = ImmutableMap.builder();

        builder.put(Pattern.compile("%f"), String::formatted);
        builder.put(Pattern.compile("%[oA]"), (s, o) -> String.valueOf(o));
        builder.put(Pattern.compile("%s"), (s, o) -> o instanceof String ? (String) o : o == null ? "null" : null);
        builder.put(Pattern.compile("%B"), (s, o) -> o instanceof Byte ? o.toString() : null);
        builder.put(Pattern.compile("%C"), (s, o) -> o instanceof Character ? o.toString() : null);
        builder.put(Pattern.compile("%S"), (s, o) -> o instanceof Short ? o.toString() : null);
        builder.put(Pattern.compile("%I"), (s, o) -> o instanceof Integer ? o.toString() : null);
        builder.put(Pattern.compile("%[JL]"), (s, o) -> o instanceof Long ? o.toString() : null);
        builder.put(Pattern.compile("%F"), (s, o) -> o instanceof Float ? o.toString() : null);
        builder.put(Pattern.compile("%D"), (s, o) -> o instanceof Double ? o.toString() : null);

        STANDARD_STRICT_FORMAT = builder.build();
    }
}
