package io.github.svegon.utils.collections;

import io.github.svegon.utils.FunctionUtil;
import io.github.svegon.utils.collections.stream.StreamUtil;
import io.github.svegon.utils.fast.util.booleans.BooleanSortedSet;
import io.github.svegon.utils.fast.util.chars.transform.booleans.Z2CTransformingSet;
import io.github.svegon.utils.fast.util.chars.transform.booleans.Z2CTransformingSortedSet;
import io.github.svegon.utils.fast.util.chars.transform.bytes.B2CTransformingSet;
import io.github.svegon.utils.fast.util.chars.transform.bytes.B2CTransformingSortedSet;
import io.github.svegon.utils.fast.util.chars.transform.chars.C2CTransformingSet;
import io.github.svegon.utils.fast.util.chars.transform.chars.C2CTransformingSortedSet;
import io.github.svegon.utils.fast.util.chars.transform.ints.I2CTransformingSet;
import io.github.svegon.utils.fast.util.chars.transform.ints.I2CTransformingSortedSet;
import io.github.svegon.utils.fast.util.chars.transform.shorts.S2CTransformingSet;
import io.github.svegon.utils.fast.util.chars.transform.shorts.S2CTransformingSortedSet;
import io.github.svegon.utils.fast.util.objects.transform.booleans.Z2LTransformingSet;
import io.github.svegon.utils.fast.util.objects.transform.booleans.Z2LTransformingSortedSet;
import io.github.svegon.utils.fast.util.objects.transform.bytes.B2LTransformingSet;
import io.github.svegon.utils.fast.util.objects.transform.bytes.B2LTransformingSortedSet;
import io.github.svegon.utils.fast.util.objects.transform.chars.C2LTransformingSet;
import io.github.svegon.utils.fast.util.objects.transform.chars.C2LTransformingSortedSet;
import io.github.svegon.utils.fast.util.objects.transform.ints.I2LTransformingSet;
import io.github.svegon.utils.fast.util.objects.transform.ints.I2LTransformingSortedSet;
import io.github.svegon.utils.fast.util.objects.transform.objects.L2LTransformingSet;
import io.github.svegon.utils.fast.util.objects.transform.shorts.S2LTransformingSet;
import io.github.svegon.utils.fast.util.objects.transform.shorts.S2LTransformingSortedSet;
import io.github.svegon.utils.fast.util.shorts.transform.booleans.Z2STransformingSet;
import io.github.svegon.utils.fast.util.shorts.transform.booleans.Z2STransformingSortedSet;
import io.github.svegon.utils.fast.util.shorts.transform.bytes.B2STransformingSet;
import io.github.svegon.utils.fast.util.shorts.transform.bytes.B2STransformingSortedSet;
import io.github.svegon.utils.fast.util.shorts.transform.chars.C2STransformingSet;
import io.github.svegon.utils.fast.util.shorts.transform.chars.C2STransformingSortedSet;
import io.github.svegon.utils.fast.util.shorts.transform.ints.I2STransformingSet;
import io.github.svegon.utils.fast.util.shorts.transform.ints.I2STransformingSortedSet;
import io.github.svegon.utils.fast.util.shorts.transform.shorts.S2STransformingSet;
import io.github.svegon.utils.fast.util.shorts.transform.shorts.S2STransformingSortedSet;
import io.github.svegon.utils.interfaces.function.Object2BooleanFunction;
import io.github.svegon.utils.interfaces.function.Object2ByteFunction;
import io.github.svegon.utils.interfaces.function.Object2CharFunction;
import io.github.svegon.utils.interfaces.function.Object2ShortFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2CharFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction;
import it.unimi.dsi.fastutil.booleans.BooleanSet;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.chars.*;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.shorts.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public final class SetUtil {
    private SetUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * (approximately) the maximum amount of elements out of which one
     * is present where a byte table set still takes less memory
     * than a hash set
     */
    public static final double BYTE_TABLE_SET_EFFECTIVE_FRAGMENTATION = 224;

    public static double fragmentation(ShortCollection c) {
        if (c.isEmpty()) {
            return 0;
        }

        short min = StreamUtil.shortStream(c.spliterator(), true).min().get();
        short max = StreamUtil.shortStream(c.spliterator(), true).max().get();

        return (max - min + 1D) / c.size();
    }

    public static double fragmentation(IntCollection c) {
        if (c.isEmpty()) {
            return 0;
        }

        int min = c.intParallelStream().min().getAsInt();
        int max = c.intParallelStream().max().getAsInt();

        return (max - min + 1D) / c.size();
    }

    public static double fragmentation(LongCollection c) {
        if (c.isEmpty()) {
            return 0;
        }

        long min = c.longParallelStream().min().getAsLong();
        long max = c.longParallelStream().max().getAsLong();

        return (max - min + 1D) / c.size();
    }

    public static double fragmentation(CharCollection c) {
        if (c.isEmpty()) {
            return 0;
        }

        char min = StreamUtil.charStream(c.spliterator(), true).min().get();
        char max = StreamUtil.charStream(c.spliterator(), true).max().get();

        return (max - min + 1D) / c.size();
    }

    public static double fragmentation(FloatCollection c) {
        if (c.isEmpty()) {
            return 0;
        }

        int min = StreamUtil.floatStream(c.spliterator(), true).mapToInt(Float::floatToIntBits).min().getAsInt();
        int max = StreamUtil.floatStream(c.spliterator(), true).mapToInt(Float::floatToIntBits).max().getAsInt();

        return (max - min + 1D) / c.size();
    }

    public static double fragmentation(DoubleCollection c) {
        if (c.isEmpty()) {
            return 0;
        }

        long min = c.doubleParallelStream().mapToLong(Double::doubleToLongBits).min().getAsLong();
        long max = c.doubleParallelStream().mapToLong(Double::doubleToLongBits).max().getAsLong();

        return (max - min + 1D) / c.size();
    }

    public static <T, E> L2LTransformingSet<T, E> transformToObj(final @NotNull Set<T> set,
                                             final @NotNull Function<? super T, ? extends E> forwardingTransformer,
                                             final @NotNull Function<? super E, ? extends T> backingTransformer) {
        return new L2LTransformingSet<>(set, forwardingTransformer, backingTransformer);
    }

    public static <T, E> L2LTransformingSet<T, E> transformToObj(final @NotNull Set<T> set,
                                             final @NotNull Function<? super T, ? extends E> forwardingTransformer) {
        return transformToObj(set, forwardingTransformer, FunctionUtil.unsupportedFunction());
    }

    public static <E> Z2LTransformingSet<E> mapToObj(final @NotNull BooleanSet set,
                                             final @NotNull Boolean2ObjectFunction<? extends E> forwardingTransformer,
                                             final @NotNull Object2BooleanFunction<? super E> backingTransformer) {
        return new Z2LTransformingSet<>(set, forwardingTransformer, backingTransformer);
    }

    public static <E> Z2LTransformingSortedSet<E> mapToObj(final @NotNull BooleanSortedSet set,
                                               final @NotNull Boolean2ObjectFunction<? extends E> forwardingTransformer,
                                               final @NotNull Object2BooleanFunction<? super E> backingTransformer) {
        return new Z2LTransformingSortedSet<>(set, forwardingTransformer, backingTransformer);
    }

    public static <E> B2LTransformingSet<E> mapToObj(final @NotNull ByteSet set,
                                                     final @NotNull Byte2ObjectFunction<? extends E> forwardingTransformer,
                                                     final @NotNull Object2ByteFunction<? super E> backingTransformer) {
        return new B2LTransformingSet<>(set, forwardingTransformer, backingTransformer);
    }

    public static <E> B2LTransformingSortedSet<E> mapToObj(final @NotNull ByteSortedSet set,
                                                           final @NotNull Byte2ObjectFunction<? extends E> forwardingTransformer,
                                                           final @NotNull Object2ByteFunction<? super E> backingTransformer) {
        return new B2LTransformingSortedSet<>(set, forwardingTransformer, backingTransformer);
    }

    public static <E> C2LTransformingSet<E> mapToObj(final @NotNull CharSet set,
                                                     final @NotNull Char2ObjectFunction<? extends E> forwardingTransformer,
                                                     final @NotNull Object2CharFunction<? super E> backingTransformer) {
        return new C2LTransformingSet<>(set, forwardingTransformer, backingTransformer);
    }

    public static <E> C2LTransformingSortedSet<E> mapToObj(final @NotNull CharSortedSet set,
                                                           final @NotNull Char2ObjectFunction<? extends E> forwardingTransformer,
                                                           final @NotNull Object2CharFunction<? super E> backingTransformer) {
        return new C2LTransformingSortedSet<>(set, forwardingTransformer, backingTransformer);
    }

    public static <E> S2LTransformingSet<E> mapToObj(final @NotNull ShortSet set,
                                                     final @NotNull Short2ObjectFunction<? extends E> forwardingTransformer,
                                                     final @NotNull Object2ShortFunction<? super E> backingTransformer) {
        return new S2LTransformingSet<>(set, forwardingTransformer, backingTransformer);
    }

    public static <E> S2LTransformingSortedSet<E> mapToObj(final @NotNull ShortSortedSet set,
                                                           final @NotNull Short2ObjectFunction<? extends E> forwardingTransformer,
                                                           final @NotNull Object2ShortFunction<? super E> backingTransformer) {
        return new S2LTransformingSortedSet<>(set, forwardingTransformer, backingTransformer);
    }

    public static <E> I2LTransformingSet<E> mapToObj(final @NotNull IntSet set,
                                                     final @NotNull IntFunction<? extends E> forwardingTransformer,
                                                     final @NotNull ToIntFunction<? super E> backingTransformer) {
        return new I2LTransformingSet<>(set, forwardingTransformer, backingTransformer);
    }

    public static <E> I2LTransformingSortedSet<E> mapToObj(final @NotNull IntSortedSet set,
                                                           final @NotNull IntFunction<? extends E> forwardingTransformer,
                                                           final @NotNull ToIntFunction<? super E> backingTransformer) {
        return new I2LTransformingSortedSet<>(set, forwardingTransformer, backingTransformer);
    }

    public static Z2CTransformingSet mapToChar(final @NotNull BooleanSet set,
                                                final @NotNull Boolean2CharFunction forwardingTransformer,
                                                final @NotNull CharPredicate backingTransformer) {
        return new Z2CTransformingSet(set, forwardingTransformer, backingTransformer);
    }

    public static Z2CTransformingSortedSet mapToChar(final @NotNull BooleanSortedSet set,
                                                     final @NotNull Boolean2CharFunction forwardingTransformer,
                                                     final @NotNull CharPredicate backingTransformer) {
        return new Z2CTransformingSortedSet(set, forwardingTransformer, backingTransformer);
    }

    public static B2CTransformingSet mapToChar(final @NotNull ByteSet set,
                                               final @NotNull Byte2CharFunction forwardingTransformer,
                                               final @NotNull Char2ByteFunction backingTransformer) {
        return new B2CTransformingSet(set, forwardingTransformer, backingTransformer);
    }

    public static B2CTransformingSortedSet mapToChar(final @NotNull ByteSortedSet set,
                                                     final @NotNull Byte2CharFunction forwardingTransformer,
                                                     final @NotNull Char2ByteFunction backingTransformer) {
        return new B2CTransformingSortedSet(set, forwardingTransformer, backingTransformer);
    }

    public static C2CTransformingSet mapToChar(final @NotNull CharSet set,
                                               final @NotNull CharUnaryOperator forwardingTransformer,
                                               final @NotNull CharUnaryOperator backingTransformer) {
        return new C2CTransformingSet(set, forwardingTransformer, backingTransformer);
    }

    public static C2CTransformingSortedSet mapToChar(final @NotNull CharSortedSet set,
                                                     final @NotNull CharUnaryOperator forwardingTransformer,
                                                     final @NotNull CharUnaryOperator backingTransformer) {
        return new C2CTransformingSortedSet(set, forwardingTransformer, backingTransformer);
    }

    public static S2CTransformingSet mapToChar(final @NotNull ShortSet set,
                                               final @NotNull Short2CharFunction forwardingTransformer,
                                               final @NotNull Char2ShortFunction backingTransformer) {
        return new S2CTransformingSet(set, forwardingTransformer, backingTransformer);
    }

    public static S2CTransformingSortedSet mapToChar(final @NotNull ShortSortedSet set,
                                                     final @NotNull Short2CharFunction forwardingTransformer,
                                                     final @NotNull Char2ShortFunction backingTransformer) {
        return new S2CTransformingSortedSet(set, forwardingTransformer, backingTransformer);
    }

    public static I2CTransformingSet mapToChar(final @NotNull IntSet set,
                                               final @NotNull Int2CharFunction forwardingTransformer,
                                               final @NotNull Char2IntFunction backingTransformer) {
        return new I2CTransformingSet(set, forwardingTransformer, backingTransformer);
    }

    public static I2CTransformingSortedSet mapToChar(final @NotNull IntSortedSet set,
                                                     final @NotNull Int2CharFunction forwardingTransformer,
                                                     final @NotNull Char2IntFunction backingTransformer) {
        return new I2CTransformingSortedSet(set, forwardingTransformer, backingTransformer);
    }

    public static Z2STransformingSet mapToShort(final @NotNull BooleanSet set,
                                                final @NotNull Boolean2ShortFunction forwardingTransformer,
                                                final @NotNull ShortPredicate backingTransformer) {
        return new Z2STransformingSet(set, forwardingTransformer, backingTransformer);
    }

    public static Z2STransformingSortedSet mapToShort(final @NotNull BooleanSortedSet set,
                                                      final @NotNull Boolean2ShortFunction forwardingTransformer,
                                                      final @NotNull ShortPredicate backingTransformer) {
        return new Z2STransformingSortedSet(set, forwardingTransformer, backingTransformer);
    }

    public static B2STransformingSet mapToShort(final @NotNull ByteSet set,
                                                final @NotNull Byte2ShortFunction forwardingTransformer,
                                                final @NotNull Short2ByteFunction backingTransformer) {
        return new B2STransformingSet(set, forwardingTransformer, backingTransformer);
    }

    public static B2STransformingSortedSet mapToShort(final @NotNull ByteSortedSet set,
                                                      final @NotNull Byte2ShortFunction forwardingTransformer,
                                                      final @NotNull Short2ByteFunction backingTransformer) {
        return new B2STransformingSortedSet(set, forwardingTransformer, backingTransformer);
    }

    public static C2STransformingSet mapToShort(final @NotNull CharSet set,
                                                final @NotNull Char2ShortFunction forwardingTransformer,
                                                final @NotNull Short2CharFunction backingTransformer) {
        return new C2STransformingSet(set, forwardingTransformer, backingTransformer);
    }

    public static C2STransformingSortedSet mapToShort(final @NotNull CharSortedSet set,
                                                      final @NotNull Char2ShortFunction forwardingTransformer,
                                                      final @NotNull Short2CharFunction backingTransformer) {
        return new C2STransformingSortedSet(set, forwardingTransformer, backingTransformer);
    }

    public static S2STransformingSet mapToShort(final @NotNull ShortSet set,
                                                final @NotNull ShortUnaryOperator forwardingTransformer,
                                                final @NotNull ShortUnaryOperator backingTransformer) {
        return new S2STransformingSet(set, forwardingTransformer, backingTransformer);
    }

    public static S2STransformingSortedSet mapToShort(final @NotNull ShortSortedSet set,
                                                      final @NotNull ShortUnaryOperator forwardingTransformer,
                                                      final @NotNull ShortUnaryOperator backingTransformer) {
        return new S2STransformingSortedSet(set, forwardingTransformer, backingTransformer);
    }

    public static I2STransformingSet mapToShort(final @NotNull IntSet set,
                                                final @NotNull Int2ShortFunction forwardingTransformer,
                                                final @NotNull Short2IntFunction backingTransformer) {
        return new I2STransformingSet(set, forwardingTransformer, backingTransformer);
    }

    public static I2STransformingSortedSet mapToShort(final @NotNull IntSortedSet set,
                                                      final @NotNull Int2ShortFunction forwardingTransformer,
                                                      final @NotNull Short2IntFunction backingTransformer) {
        return new I2STransformingSortedSet(set, forwardingTransformer, backingTransformer);
    }
}
