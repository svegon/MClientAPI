package com.github.svegon.utils.collections;

import com.github.svegon.utils.FunctionUtil;
import com.github.svegon.utils.fast.util.bytes.transform.bytes.B2BTransformingMultiset;
import com.github.svegon.utils.fast.util.bytes.transform.objects.L2BTransformingMultiset;
import com.github.svegon.utils.fast.util.bytes.transform.shorts.S2BTransformingMultiset;
import com.github.svegon.utils.fast.util.chars.transform.bytes.B2CTransformingMultiset;
import com.github.svegon.utils.fast.util.chars.transform.chars.C2CTransformingMultiset;
import com.github.svegon.utils.fast.util.chars.transform.objects.L2CTransformingMultiset;
import com.github.svegon.utils.fast.util.ints.transform.bytes.B2ITransformingMultiset;
import com.github.svegon.utils.fast.util.ints.transform.chars.C2ITransformingMultiset;
import com.github.svegon.utils.fast.util.ints.transform.ints.I2ITransformingMultiset;
import com.github.svegon.utils.fast.util.ints.transform.shorts.S2ITransformingMultiset;
import com.github.svegon.utils.fast.util.objects.transform.objects.L2LBackedTransformingMultiset;
import com.github.svegon.utils.fast.util.objects.transform.objects.L2LTransformingMultiset;
import com.github.svegon.utils.fast.util.shorts.transform.bytes.B2STransformingMultiset;
import com.github.svegon.utils.fast.util.shorts.transform.chars.C2STransformingMultiset;
import com.github.svegon.utils.fast.util.shorts.transform.ints.I2STransformingMultiset;
import com.github.svegon.utils.fast.util.shorts.transform.objects.L2STransformingMultiset;
import com.github.svegon.utils.fast.util.shorts.transform.shorts.S2STransformingMultiset;
import com.github.svegon.utils.interfaces.function.Object2ByteFunction;
import com.github.svegon.utils.interfaces.function.Object2CharFunction;
import com.github.svegon.utils.interfaces.function.Object2ShortFunction;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.chars.*;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.shorts.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;

public final class CollectionUtil {
    private CollectionUtil() {
        throw new AssertionError();
    }

    public static <E> MutableCombinedCollection<E> newMutableCombinedCollection(
            @NotNull final Collection<? extends E>... collections) {
        return new MutableCombinedCollection<>(collections);
    }

    @SuppressWarnings("unchecked")
    public static <E> MutableCombinedCollection<E> newMutableCombinedCollection(
            @NotNull final Iterable<? extends Collection<? extends E>> collections) {
        return collections instanceof Collection ? new MutableCombinedCollection<>(
                (Collection<? extends E>) collections) : newMutableCombinedCollection(collections.iterator());
    }

    public static <E> MutableCombinedCollection<E> newMutableCombinedCollection(
            @NotNull final Iterator<? extends Collection<? extends E>> collections) {
        MutableCombinedCollection<E> c = new MutableCombinedCollection<>();

        collections.forEachRemaining(c::addAll);

        return c;
    }

    public static <E> Multiset<E> collectionView(final @NotNull Iterable<E> iterable) {
        return new AbstractMultiset<>() {
            @Override
            public @NotNull Iterator<E> iterator() {
                return iterable.iterator();
            }

            @Override
            public int size() {
                return Iterables.size(iterable);
            }

            @Override
            public boolean isEmpty() {
                return !iterator().hasNext();
            }
        };
    }

    public static <T, E> L2LTransformingMultiset<T, E> transformToObj(final @NotNull Collection<T> c,
                                              final @NotNull Function<? super T, ? extends E> forwardingTransformer,
                                              final @NotNull Function<? super E, ? extends T> backingTransformer) {
        return new L2LBackedTransformingMultiset<>(c, forwardingTransformer, backingTransformer);
    }

    public static <T, E> L2LTransformingMultiset<T, E> transformToObj(final @NotNull Collection<T> c,
                                              final @NotNull Function<? super T, ? extends E> forwardingTransformer) {
        return new L2LTransformingMultiset<>(c, forwardingTransformer);
    }

    public static <T> L2BTransformingMultiset<T> transformToByte(final @NotNull Collection<T> c,
                                                                 final @NotNull Object2ByteFunction<? super T> forwardingTransformer,
                                                                 final @NotNull Byte2ObjectFunction<? extends T> backingTransformer) {
        return new L2BTransformingMultiset<>(c, forwardingTransformer, backingTransformer);
    }

    public static <T> L2BTransformingMultiset<T> transformToByte(final @NotNull Collection<T> c,
                                                                 final @NotNull Object2ByteFunction<? super T> forwardingTransformer) {
        return transformToByte(c, forwardingTransformer, FunctionUtil.unsupportedByte2ObjectFunction());
    }

    public static B2BTransformingMultiset mapToByte(final @NotNull ByteCollection c,
                                                    final @NotNull ByteUnaryOperator forwardingTransformer,
                                                    final @NotNull ByteUnaryOperator backingTransformer) {
        return new B2BTransformingMultiset(c, forwardingTransformer, backingTransformer);
    }

    public static B2BTransformingMultiset mapToByte(final @NotNull ByteCollection c,
                                                    final @NotNull ByteUnaryOperator forwardingTransformer) {
        return mapToByte(c, forwardingTransformer, FunctionUtil.unsupportedByte2ByteFunction());
    }

    public static S2BTransformingMultiset mapToByte(final @NotNull ShortCollection c,
                                                    final @NotNull Short2ByteFunction forwardingTransformer,
                                                    final @NotNull Byte2ShortFunction backingTransformer) {
        return new S2BTransformingMultiset(c, forwardingTransformer, backingTransformer);
    }

    public static S2BTransformingMultiset mapToByte(final @NotNull ShortCollection c,
                                                    final @NotNull Short2ByteFunction forwardingTransformer) {
        return mapToByte(c, forwardingTransformer, FunctionUtil.unsupportedByte2ShortFunction());
    }

    public static <T> L2CTransformingMultiset<T> transformToChar(final @NotNull Collection<T> c,
                                                                  final @NotNull Object2CharFunction<? super T> forwardingTransformer,
                                                                  final @NotNull Char2ObjectFunction<? extends T> backingTransformer) {
        return new L2CTransformingMultiset<>(c, forwardingTransformer, backingTransformer);
    }

    public static <T> L2CTransformingMultiset<T> transformToChar(final @NotNull Collection<T> c,
                                                                 final @NotNull Object2CharFunction<? super T> forwardingTransformer) {
        return transformToChar(c, forwardingTransformer, FunctionUtil.unsupportedChar2ObjectFunction());
    }

    public static B2CTransformingMultiset mapToChar(final @NotNull ByteCollection c,
                                                     final @NotNull Byte2CharFunction forwardingTransformer,
                                                     final @NotNull Char2ByteFunction backingTransformer) {
        return new B2CTransformingMultiset(c, forwardingTransformer, backingTransformer);
    }

    public static B2CTransformingMultiset mapToChar(final @NotNull ByteCollection c,
                                                    final @NotNull Byte2CharFunction forwardingTransformer) {
        return mapToChar(c, forwardingTransformer, FunctionUtil.unsupportedChar2ByteFunction());
    }

    public static C2CTransformingMultiset mapToChar(final @NotNull CharCollection c,
                                                     final @NotNull CharUnaryOperator forwardingTransformer,
                                                     final @NotNull CharUnaryOperator backingTransformer) {
        return new C2CTransformingMultiset(c, forwardingTransformer, backingTransformer);
    }

    public static C2CTransformingMultiset mapToChar(final @NotNull CharCollection c,
                                                    final @NotNull CharUnaryOperator forwardingTransformer) {
        return mapToChar(c, forwardingTransformer, FunctionUtil.unsupportedChar2CharFunction());
    }

    public static <T> L2STransformingMultiset<T> transformToShort(final @NotNull Collection<T> c,
                                                                  final @NotNull Object2ShortFunction<? super T> forwardingTransformer,
                                                                  final @NotNull Short2ObjectFunction<? extends T> backingTransformer) {
        return new L2STransformingMultiset<>(c, forwardingTransformer, backingTransformer);
    }

    public static <T> L2STransformingMultiset<T> transformToShort(final @NotNull Collection<T> c,
                                              final @NotNull Object2ShortFunction<? super T> forwardingTransformer) {
        return transformToShort(c, forwardingTransformer, FunctionUtil.unsupportedShort2ObjectFunction());
    }

    public static <T> L2STransformingMultiset<T> mapToShort(final @NotNull ObjectCollection<T> c,
                                              final @NotNull Object2ShortFunction<? super T> forwardingTransformer) {
        return transformToShort(c, forwardingTransformer, FunctionUtil.unsupportedShort2ObjectFunction());
    }

    public static B2STransformingMultiset mapToShort(final @NotNull ByteCollection c,
                                                     final @NotNull Byte2ShortFunction forwardingTransformer,
                                                     final @NotNull Short2ByteFunction backingTransformer) {
        return new B2STransformingMultiset(c, forwardingTransformer, backingTransformer);
    }

    public static B2STransformingMultiset mapToShort(final @NotNull ByteCollection c,
                                                     final @NotNull Byte2ShortFunction forwardingTransformer) {
        return mapToShort(c, forwardingTransformer, FunctionUtil.unsupportedShort2ByteFunction());
    }

    public static C2STransformingMultiset mapToShort(final @NotNull CharCollection c,
                                                     final @NotNull Char2ShortFunction forwardingTransformer,
                                                     final @NotNull Short2CharFunction backingTransformer) {
        return new C2STransformingMultiset(c, forwardingTransformer, backingTransformer);
    }

    public static C2STransformingMultiset mapToShort(final @NotNull CharCollection c,
                                                     final @NotNull Char2ShortFunction forwardingTransformer) {
        return mapToShort(c, forwardingTransformer, FunctionUtil.unsupportedShort2CharFunction());
    }

    public static S2STransformingMultiset mapToShort(final @NotNull ShortCollection c,
                                                     final @NotNull ShortUnaryOperator forwardingTransformer,
                                                     final @NotNull ShortUnaryOperator backingTransformer) {
        return new S2STransformingMultiset(c, forwardingTransformer, backingTransformer);
    }

    public static S2STransformingMultiset mapToShort(final @NotNull ShortCollection c,
                                                     final @NotNull ShortUnaryOperator forwardingTransformer) {
        return mapToShort(c, forwardingTransformer, FunctionUtil.unsupportedShort2ShortFunction());
    }

    public static I2STransformingMultiset mapToShort(final @NotNull IntCollection c,
                                                     final @NotNull Int2ShortFunction forwardingTransformer,
                                                     final @NotNull Short2IntFunction backingTransformer) {
        return new I2STransformingMultiset(c, forwardingTransformer, backingTransformer);
    }

    public static I2STransformingMultiset mapToShort(final @NotNull IntCollection c,
                                                     final @NotNull Int2ShortFunction forwardingTransformer) {
        return mapToShort(c, forwardingTransformer, FunctionUtil.unsupportedShort2IntFunction());
    }

    public static B2ITransformingMultiset mapToInt(final @NotNull ByteCollection c,
                                                   final @NotNull Byte2IntFunction forwardingTransformer,
                                                   final @NotNull Int2ByteFunction backingTransformer) {
        return new B2ITransformingMultiset(c, forwardingTransformer, backingTransformer);
    }

    public static B2ITransformingMultiset mapToInt(final @NotNull ByteCollection c,
                                                   final @NotNull Byte2IntFunction forwardingTransformer) {
        return mapToInt(c, forwardingTransformer, FunctionUtil.unsupportedInt2ByteFunction());
    }

    public static C2ITransformingMultiset mapToInt(final @NotNull CharCollection c,
                                                   final @NotNull Char2IntFunction forwardingTransformer,
                                                   final @NotNull Int2CharFunction backingTransformer) {
        return new C2ITransformingMultiset(c, forwardingTransformer, backingTransformer);
    }

    public static C2ITransformingMultiset mapToInt(final @NotNull CharCollection c,
                                                   final @NotNull Char2IntFunction forwardingTransformer) {
        return mapToInt(c, forwardingTransformer, FunctionUtil.unsupportedInt2CharFunction());
    }

    public static S2ITransformingMultiset mapToInt(final @NotNull ShortCollection c,
                                                   final @NotNull Short2IntFunction forwardingTransformer,
                                                   final @NotNull Int2ShortFunction backingTransformer) {
        return new S2ITransformingMultiset(c, forwardingTransformer, backingTransformer);
    }

    public static S2ITransformingMultiset mapToInt(final @NotNull ShortCollection c,
                                                   final @NotNull Short2IntFunction forwardingTransformer) {
        return mapToInt(c, forwardingTransformer, FunctionUtil.unsupportedInt2ShortFunction());
    }

    public static I2ITransformingMultiset mapToInt(final @NotNull IntCollection c,
                                                   final @NotNull IntUnaryOperator forwardingTransformer,
                                                   final @NotNull IntUnaryOperator backingTransformer) {
        return new I2ITransformingMultiset(c, forwardingTransformer, backingTransformer);
    }

    public static I2ITransformingMultiset mapToInt(final @NotNull IntCollection c,
                                                   final @NotNull IntUnaryOperator forwardingTransformer) {
        return mapToInt(c, forwardingTransformer, FunctionUtil.unsupportedInt2IntFunction());
    }
}
