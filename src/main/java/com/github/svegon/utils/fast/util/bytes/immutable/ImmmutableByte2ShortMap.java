package com.github.svegon.utils.fast.util.bytes.immutable;

import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.ShortBinaryOperator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiFunction;

@Immutable
public abstract class ImmmutableByte2ShortMap implements Byte2ShortMap {
    private final int hashCode;

    ImmmutableByte2ShortMap(int hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public final int hashCode() {
        return hashCode;
    }

    @Override
    public ImmmutableByte2ShortMap clone() {
        return this;
    }

    @Override
    public abstract int size();

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public final void putAll(@NotNull Map<? extends Byte, ? extends Short> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void defaultReturnValue(short rv) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final short defaultReturnValue() {
        return 0;
    }

    @Override
    public abstract ObjectSet<Entry> byte2ShortEntrySet();

    @Override
    public abstract ImmutableByteSet keySet();

    @Override
    public abstract ShortCollection values();

    @Override
    public abstract short get(byte key);

    @Override
    public abstract boolean containsKey(byte key);

    @Override
    public abstract boolean containsValue(short value);

    @Override
    public abstract short getOrDefault(byte key, short defaultValue);

    @Override
    public final short putIfAbsent(byte key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean remove(byte key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean replace(byte key, short oldValue, short newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final short computeIfAbsent(byte key, Byte2ShortFunction mappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final short compute(byte key, BiFunction<? super Byte, ? super Short, ? extends Short> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final short mergeShort(byte key, short value, ShortBinaryOperator remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final short computeIfPresent(byte key,
                                        BiFunction<? super Byte, ? super Short, ? extends Short> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    public static ImmutableByte2ShortSortedMap copyOf(final @NotNull Byte2ShortMap map) {
        if (map instanceof ImmutableByte2ShortSortedMap) {
            return (ImmutableByte2ShortSortedMap) map;
        }

        return ImmutableByte2ShortSortedMap.copyOf(null, map);
    }

    public static ImmutableByte2ShortSortedMap copyOf(final @NotNull Iterable<Entry> entries) {
        return ImmutableByte2ShortSortedMap.copyOf(null, entries);
    }

    public static ImmutableByte2ShortEmptySortedMap of() {
        return ImmutableByte2ShortEmptySortedMap.DEFAULT_SORTED;
    }

    public static ImmutableByte2ShortSingletonSortedMap of(final byte key, final short value) {
        return ImmutableByte2ShortSortedMap.of(null, key, value);
    }

    public static ImmutableByte2ShortSortedMap of(final @Nullable ByteComparator comparator, final byte k0,
                                                  final short v0, final byte k1, final short v1) {
        return ImmutableByte2ShortSortedMap.of(null, k0, v0, k1, v1);
    }

    public static ImmutableByte2ShortSortedMap of(final @Nullable ByteComparator comparator, final byte k0,
                                                  final short v0, final byte k1, final short v1,
                                                  final byte k2, final short v2) {
        return ImmutableByte2ShortSortedMap.of(null, k0, v0, k1, v1, k2, v2);
    }

    public static ImmutableByte2ShortSortedMap of(final @Nullable ByteComparator comparator, final byte k0,
                                                  final short v0, final byte k1, final short v1,
                                                  final byte k2, final short v2, final byte k3, final short v3) {
        return ImmutableByte2ShortSortedMap.of(null, k0, v0, k1, v1, k2, v2, k3, v3);
    }

    public static ImmutableByte2ShortSortedMap of(final @Nullable ByteComparator comparator, final byte k0,
                                                  final short v0, final byte k1, final short v1,
                                                  final byte k2, final short v2, final byte k3, final short v3,
                                                  final byte k4, final short v4) {
        return ImmutableByte2ShortSortedMap.of(null, k0, v0, k1, v1, k2, v2, k3, v3, k4, v4);
    }

    public static ImmutableByte2ShortSortedMap of(final @Nullable ByteComparator comparator, final byte k0,
                                                  final short v0, final byte k1, final short v1,
                                                  final byte k2, final short v2, final byte k3, final short v3,
                                                  final byte k4, final short v4, final byte k5, final short v5) {
        return ImmutableByte2ShortSortedMap.of(null, k0, v0, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }
}
