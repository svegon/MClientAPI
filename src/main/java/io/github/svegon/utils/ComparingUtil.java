package io.github.svegon.utils;

import io.github.svegon.utils.interfaces.function.Object2ByteFunction;
import io.github.svegon.utils.interfaces.function.Object2CharFunction;
import io.github.svegon.utils.interfaces.function.Object2FloatFunction;
import io.github.svegon.utils.interfaces.function.Object2ShortFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.BooleanComparator;
import it.unimi.dsi.fastutil.bytes.ByteComparator;
import it.unimi.dsi.fastutil.chars.CharComparator;
import it.unimi.dsi.fastutil.doubles.DoubleComparator;
import it.unimi.dsi.fastutil.floats.FloatComparator;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.longs.LongComparator;
import it.unimi.dsi.fastutil.shorts.ShortComparator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public final class ComparingUtil {
    private ComparingUtil() {
        throw new UnsupportedOperationException();
    }

    private static final Comparator EVEN_COMPARATOR = (o0, o1) -> 0;
    private static final Comparator<String> ALPHABETIC = ((Comparator<String>) String::compareToIgnoreCase)
            .thenComparing(String::compareTo);

    @SuppressWarnings("unchecked")
    public static <T> Comparator<T> even() {
        return (Comparator<T>) EVEN_COMPARATOR;
    }

    public static Comparator<String> alphabeticOrder() {
        return ALPHABETIC;
    }

    // comparing as true > false
    public static <T> Comparator<T> comparingBoolean(final @NotNull Predicate<? super T> keyExtractor) {
        Preconditions.checkNotNull(keyExtractor);
        return (o1, o2) -> Boolean.compare(keyExtractor.test(o1), keyExtractor.test(o2));
    }

    public static <T> Comparator<T> comparingFloat(final @NotNull Object2FloatFunction<? super T> keyExtractor) {
        Preconditions.checkNotNull(keyExtractor);
        return (o1, o2) -> Float.compare(keyExtractor.applyAsFloat(o1), keyExtractor.applyAsFloat(o2));
    }

    // comparing as true > false
    public static <T> Comparator<T> comparingBoolean(final @NotNull Predicate<? super T> keyExtractor,
                                                     final @Nullable BooleanComparator comparator) {
        Preconditions.checkNotNull(keyExtractor);
        return comparator == null ? comparingBoolean(keyExtractor)
                : (o1, o2) -> comparator.compare(keyExtractor.test(o1), keyExtractor.test(o2));
    }

    public static <T> Comparator<T> comparingByte(final @NotNull Object2ByteFunction<? super T> keyExtractor,
                                                  final @Nullable ByteComparator comparator) {
        Preconditions.checkNotNull(keyExtractor);
        return comparator == null ? Comparator.comparingInt(keyExtractor)
                : (o1, o2) -> comparator.compare(keyExtractor.applyAsByte(o1), keyExtractor.applyAsByte(o2));
    }

    public static <T> Comparator<T> comparingChar(final @NotNull Object2CharFunction<? super T> keyExtractor,
                                                  final @Nullable CharComparator comparator) {
        Preconditions.checkNotNull(keyExtractor);
        return comparator == null ? Comparator.comparingInt(keyExtractor)
                : (o1, o2) -> comparator.compare(keyExtractor.applyAsChar(o1), keyExtractor.applyAsChar(o2));
    }

    public static <T> Comparator<T> comparingShort(final @NotNull Object2ShortFunction<? super T> keyExtractor,
                                                   final @Nullable ShortComparator comparator) {
        Preconditions.checkNotNull(keyExtractor);
        return comparator == null ? Comparator.comparingInt(keyExtractor)
                : (o1, o2) -> comparator.compare(keyExtractor.applyAsShort(o1), keyExtractor.applyAsShort(o2));
    }

    public static <T> Comparator<T> comparingInt(final @NotNull ToIntFunction<? super T> keyExtractor,
                                                   final @Nullable IntComparator comparator) {
        Preconditions.checkNotNull(keyExtractor);
        return comparator == null ? Comparator.comparingInt(keyExtractor)
                : (o1, o2) -> comparator.compare(keyExtractor.applyAsInt(o1), keyExtractor.applyAsInt(o2));
    }

    public static <T> Comparator<T> comparingLong(final @NotNull ToLongFunction<? super T> keyExtractor,
                                                   final @Nullable LongComparator comparator) {
        Preconditions.checkNotNull(keyExtractor);
        return comparator == null ? Comparator.comparingLong(keyExtractor)
                : (o1, o2) -> comparator.compare(keyExtractor.applyAsLong(o1), keyExtractor.applyAsLong(o2));
    }

    public static <T> Comparator<T> comparingFloat(final @NotNull Object2FloatFunction<? super T> keyExtractor,
                                                   final @Nullable FloatComparator comparator) {
        Preconditions.checkNotNull(keyExtractor);
        return comparator == null ? comparingFloat(keyExtractor)
                : (o1, o2) -> comparator.compare(keyExtractor.applyAsFloat(o1), keyExtractor.applyAsFloat(o2));
    }

    public static <T> Comparator<T> comparingDouble(final @NotNull ToDoubleFunction<? super T> keyExtractor,
                                                 final @Nullable DoubleComparator comparator) {
        Preconditions.checkNotNull(keyExtractor);
        return comparator == null ? Comparator.comparingDouble(keyExtractor)
                : (o1, o2) -> comparator.compare(keyExtractor.applyAsDouble(o1), keyExtractor.applyAsDouble(o2));
    }

    public static <T> Comparator<T> prioritize(final T priority,
                                               final @NotNull Comparator<? super T> secondaryComparator) {
        Preconditions.checkNotNull(secondaryComparator);

        return priority == null ? Comparator.nullsLast(secondaryComparator) : (o0, o1) -> {
            if (priority.equals(o0)) {
                return 1;
            }

            if (priority.equals(o1)) {
                return -1;
            }

            return secondaryComparator.compare(o0, o1);
        };
    }

    public static <T> Comparator<T> prioritize(final T god) {
        return prioritize(god, even());
    }

    @SuppressWarnings("unchecked")
    public static <E> int compare(final @Nullable E a, final @Nullable E b) {
        if (a == null) {
            return b == null ? 0 : -((Comparable<E>) b).compareTo(a);
        }

        return ((Comparable<E>) a).compareTo(b);
    }

    public static <E> int compare(final @Nullable Comparator<? super E> comparator, @Nullable E a, @Nullable E b) {
        if (comparator == null) {
            return compare(a, b);
        }

        return comparator.compare(a, b);
    }

    public static int compare(final @Nullable BooleanComparator comparator, boolean a, boolean b) {
        if (comparator == null) {
            return Boolean.compare(a, b);
        }

        return comparator.compare(a, b);
    }

    public static int compare(final @Nullable ByteComparator comparator, byte a, byte b) {
        if (comparator == null) {
            return Byte.compare(a, b);
        }

        return comparator.compare(a, b);
    }

    public static int compare(final @Nullable CharComparator comparator, char a, char b) {
        if (comparator == null) {
            return Character.compare(a, b);
        }

        return comparator.compare(a, b);
    }

    public static int compare(final @Nullable ShortComparator comparator, short a, short b) {
        if (comparator == null) {
            return Short.compare(a, b);
        }

        return comparator.compare(a, b);
    }

    public static int compare(final @Nullable IntComparator comparator, int a, int b) {
        if (comparator == null) {
            return Integer.compare(a, b);
        }

        return comparator.compare(a, b);
    }

    public static int compare(final @Nullable LongComparator comparator, long a, long b) {
        if (comparator == null) {
            return Long.compare(a, b);
        }

        return comparator.compare(a, b);
    }

    public static int compare(final @Nullable FloatComparator comparator, final float a, final float b) {
        if (comparator == null) {
            return Float.compare(a, b);
        }

        return comparator.compare(a, b);
    }

    public static int compare(final @Nullable DoubleComparator comparator, double a, double b) {
        if (comparator == null) {
            return Double.compare(a, b);
        }

        return comparator.compare(a, b);
    }
}
