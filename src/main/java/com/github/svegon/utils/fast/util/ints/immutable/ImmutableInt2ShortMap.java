package com.github.svegon.utils.fast.util.ints.immutable;

import com.github.svegon.utils.collections.MapType;
import com.github.svegon.utils.collections.MapUtil;
import com.github.svegon.utils.fast.util.shorts.ShortMultiset;
import com.github.svegon.utils.hash.HashUtil;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.ShortBinaryOperator;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.function.BiFunction;

@Immutable
public abstract class ImmutableInt2ShortMap implements Int2ShortMap {
    private final int hashCode;

    ImmutableInt2ShortMap(int hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public final int hashCode() {
        return hashCode;
    }

    @Override
    public ImmutableInt2ShortMap clone() {
        return this;
    }

    @Override
    public abstract int size();

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public final void putAll(@NotNull Map<? extends Integer, ? extends Short> m) {
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
    public abstract ObjectSet<Entry> int2ShortEntrySet();

    @Override
    public abstract ImmutableIntSet keySet();

    @Override
    public abstract ShortMultiset values();

    @Override
    public short get(int key) {
        return getOrDefault(key, defaultReturnValue());
    }

    @Override
    public abstract boolean containsKey(int key);

    @Override
    public abstract boolean containsValue(short value);

    @Override
    public abstract short getOrDefault(int key, short defaultValue);

    @Override
    public final short putIfAbsent(int key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean remove(int key, short value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean replace(int key, short oldValue, short newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final short computeIfAbsent(int key, Int2ShortFunction mappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final short compute(int key, BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final short mergeShort(int key, short value, ShortBinaryOperator remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final short computeIfPresent(int key,
                                        BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    public static ImmutableInt2ShortHashMap hashMap(@Nullable IntHash.Strategy strategy,
                                                         final @NotNull Iterable<Entry> entries) {
        if (strategy == null) {
            strategy = HashUtil.defaultIntStrategy();
        }

        Int2ShortOpenCustomHashMap map = new Int2ShortOpenCustomHashMap(strategy);

        if (entries instanceof FastEntrySet) {
            ((FastEntrySet) entries).fastForEach(e -> map.put(e.getIntKey(), e.getShortValue()));
        } else {
            entries.forEach(e -> map.put(e.getIntKey(), e.getShortValue()));
        }

        return new ImmutableInt2ShortHashMap(map);
    }

    public static ImmutableInt2ShortForkTableMap forkTableMap(@Nullable IntHash.Strategy strategy,
                                                              final @NotNull Iterable<Entry> entries) {
        if (strategy == null) {
            strategy = HashUtil.defaultIntStrategy();
        }

        Int2ShortOpenCustomHashMap map = new Int2ShortOpenCustomHashMap(strategy);

        if (entries instanceof FastEntrySet) {
            ((FastEntrySet) entries).fastForEach(e -> map.put(e.getIntKey(), e.getShortValue()));
        } else {
            entries.forEach(e -> map.put(e.getIntKey(), e.getShortValue()));
        }

        return new ImmutableInt2ShortForkTableMap(map);
    }

    public static ImmutableInt2ShortMap copyOf(final @NotNull Int2ShortMap map,
                                               final @NotNull Comparator<? super MapType> priority) {
        if (map instanceof ImmutableInt2ShortMap) {
            return (ImmutableInt2ShortMap) map;
        }

        return copyOf(map.int2ShortEntrySet(), priority);
    }

    public static ImmutableInt2ShortMap copyOf(final @NotNull Int2ShortMap map) {
        if (map instanceof ImmutableInt2ShortMap) {
            return (ImmutableInt2ShortMap) map;
        }

        return ImmutableInt2ShortSortedMap.copyOf(null, map);
    }

    public static ImmutableInt2ShortMap copyOf(final @NotNull Iterable<Entry> entries,
                                               final @NotNull Comparator<? super MapType> priority) {
        if (entries instanceof Collection<?> c) {
            if (c.size() > MapUtil.HASH_MAP_EFFECTIVITY) {
                switch (Arrays.stream(ImmutableInt2IntSortedMap.MAP_TYPES).max(priority).get()) {
                    case HASH -> {
                        return hashMap(HashUtil.defaultIntStrategy(), entries);
                    }
                    case TABLE_FORK -> {
                        return forkTableMap(HashUtil.defaultIntStrategy(), entries);
                    }
                }
            }
        }

        return ImmutableInt2ShortSortedMap.copyOf(null, entries);
    }

    public static ImmutableInt2ShortSortedMap copyOf(final @NotNull Iterable<Entry> entries) {
        return ImmutableInt2ShortSortedMap.copyOf(null, entries);
    }

    public static ImmutableInt2ShortEmptySortedMap of() {
        return ImmutableInt2ShortEmptySortedMap.DEFAULT_SORTED;
    }

    public static ImmutableInt2ShortSingletonSortedMap of(final int key, final short value) {
        return ImmutableInt2ShortSortedMap.of(null, key, value);
    }

    public static ImmutableInt2ShortSortedMap of(final int k0,
                                                  final short v0, final int k1, final short v1) {
        return ImmutableInt2ShortSortedMap.of(null, k0, v0, k1, v1);
    }

    public static ImmutableInt2ShortSortedMap of(final int k0,
                                                  final short v0, final int k1, final short v1,
                                                  final int k2, final short v2) {
        return ImmutableInt2ShortSortedMap.of(null, k0, v0, k1, v1, k2, v2);
    }

    public static ImmutableInt2ShortSortedMap of(final int k0,
                                                  final short v0, final int k1, final short v1,
                                                  final int k2, final short v2, final int k3, final short v3) {
        return ImmutableInt2ShortSortedMap.of(null, k0, v0, k1, v1, k2, v2, k3, v3);
    }

    public static ImmutableInt2ShortSortedMap of(final int k0,
                                                  final short v0, final int k1, final short v1,
                                                  final int k2, final short v2, final int k3, final short v3,
                                                  final int k4, final short v4) {
        return ImmutableInt2ShortSortedMap.of(null, k0, v0, k1, v1, k2, v2, k3, v3, k4, v4);
    }

    public static ImmutableInt2ShortSortedMap of(final int k0,
                                                 final short v0, final int k1, final short v1,
                                                 final int k2, final short v2, final int k3, final short v3,
                                                 final int k4, final short v4, final int k5, final short v5) {
        return ImmutableInt2ShortSortedMap.of(null, k0, v0, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    public static ImmutableInt2ShortSortedMap of(final @NotNull Comparator<? super MapType> priority,
                                                 final int @NotNull [] keys, final short @NotNull ... values) {
        return ImmutableInt2ShortSortedMap.of(new Int2ShortOpenCustomHashMap(keys, values,
                HashUtil.defaultIntStrategy()), null, priority);
    }

    public static ImmutableInt2ShortSortedMap of(final int @NotNull [] keys, final short @NotNull ... values) {
        return of(MapType.memoryPriority(), keys, values);
    }
}
