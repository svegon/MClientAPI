package com.github.svegon.utils.fast.util.ints.immutable;

import com.github.svegon.utils.ComparingUtil;
import com.github.svegon.utils.collections.MapType;
import com.github.svegon.utils.collections.SetUtil;
import com.github.svegon.utils.fast.util.shorts.ShortMultiset;
import com.github.svegon.utils.hash.HashUtil;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.EnumSet;

@Immutable
public abstract class ImmutableInt2ShortSortedMap extends ImmutableInt2ShortMap implements Int2ShortSortedMap,
        IntComparator {
    ImmutableInt2ShortSortedMap(int hashCode) {
        super(hashCode);
    }

    @Override
    public final ImmutableInt2ShortSortedMap clone() {
        return this;
    }

    @Override
    public abstract ObjectSortedSet<Entry> int2ShortEntrySet();

    @Override
    public abstract ImmutableIntSortedSet keySet();

    @Override
    public abstract ShortMultiset values();

    @Override
    public abstract ImmutableInt2ShortSortedMap subMap(int fromKey, int toKey);

    @Override
    public abstract ImmutableInt2ShortSortedMap headMap(int toKey);

    @Override
    public abstract ImmutableInt2ShortSortedMap tailMap(int fromKey);

    @Override
    public abstract int firstIntKey();

    @Override
    public abstract int lastIntKey();

    @Override
    public abstract IntComparator comparator();

    @Override
    public int compare(int k1, int k2) {
        return ComparingUtil.compare(comparator(), k1, k2);
    }

    public static ImmutableInt2ShortHashSortedMap hashMap(final @Nullable IntComparator comparator,
                                                          @Nullable IntHash.Strategy strategy,
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

        return new ImmutableInt2ShortHashSortedMap(map, comparator);
    }

    public static ImmutableInt2ShortForkTableSortedMap forkTableMap(final @Nullable IntComparator comparator,
                                                              @Nullable IntHash.Strategy strategy,
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

        return comparator == null ? new ImmutableInt2ShortForkTableDefaultSortedMap(map, strategy)
                : new ImmutableInt2ShortForkTableSortedMap(map, strategy, comparator);
    }

    public static ImmutableInt2ShortSortedMap copyOf(final @NotNull Int2ShortMap map,
                                                              final @Nullable IntComparator comparator,
                                                              final @Nullable IntHash.Strategy strategy) {
        Int2ShortOpenCustomHashMap values = new Int2ShortOpenCustomHashMap(map, HashUtil.defaultIntStrategy());

        if (values.isEmpty()) {
            return of(comparator);
        }

        return SetUtil.fragmentation(values.keySet()) < MapType.HASH.memoryEffectiveFragmentation()
                ? hashMap(comparator, strategy, values)
                : forkTableMap(comparator, strategy, values);
    }

    public static ImmutableInt2ShortSortedMap copyOf(final @Nullable IntComparator comparator,
                                                     final @NotNull Int2ShortMap map) {
        return of(new Int2ShortOpenCustomHashMap(map, HashUtil.defaultIntStrategy())
                , comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2ShortSortedMap copyOf(final @NotNull Int2ShortSortedMap map) {
        if (map instanceof ImmutableInt2ShortSortedMap) {
            return (ImmutableInt2ShortSortedMap) map;
        }

        return copyOf(map.comparator(), map);
    }

    public static ImmutableInt2ShortSortedMap copyOf(final @Nullable IntComparator comparator,
                                                     final @NotNull Iterable<Entry> entries) {
        Int2ShortOpenCustomHashMap values = new Int2ShortOpenCustomHashMap(HashUtil.defaultIntStrategy());

        for (Entry entry : entries) {
            values.put(entry.getIntKey(), entry.getShortValue());
        }

        return of(values, comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2ShortEmptySortedMap of(final @Nullable IntComparator comparator) {
        return comparator == null ? ImmutableInt2ShortEmptySortedMap.DEFAULT_SORTED
                : new ImmutableInt2ShortEmptySortedMap(comparator);
    }

    public static ImmutableInt2ShortSingletonSortedMap of(final @Nullable IntComparator comparator, final int key,
                                                           final short value) {
        return new ImmutableInt2ShortSingletonSortedMap(ImmutableIntSortedSet.of(comparator, key), value);
    }

    public static ImmutableInt2ShortSortedMap of(final @Nullable IntComparator comparator, final int k0,
                                                 final short v0, final int k1, final short v1) {
        Int2ShortOpenCustomHashMap map = new Int2ShortOpenCustomHashMap(HashUtil.defaultIntStrategy());

        map.put(k0, v0);
        map.put(k1, v1);

        return of(map, comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2ShortSortedMap of(final @Nullable IntComparator comparator, final int k0,
                                                 final short v0, final int k1, final short v1,
                                                 final int k2, final short v2) {
        Int2ShortOpenCustomHashMap map = new Int2ShortOpenCustomHashMap(HashUtil.defaultIntStrategy());

        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);

        return of(map, comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2ShortSortedMap of(final @Nullable IntComparator comparator, final int k0,
                                                 final short v0, final int k1, final short v1,
                                                 final int k2, final short v2, final int k3, final short v3) {
        Int2ShortOpenCustomHashMap map = new Int2ShortOpenCustomHashMap(HashUtil.defaultIntStrategy());

        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);

        return of(map, comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2ShortSortedMap of(final @Nullable IntComparator comparator, final int k0,
                                                 final short v0, final int k1, final short v1,
                                                 final int k2, final short v2, final int k3, final short v3,
                                                 final int k4, final short v4) {
        Int2ShortOpenCustomHashMap map = new Int2ShortOpenCustomHashMap(HashUtil.defaultIntStrategy());

        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);

        return of(map, comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2ShortSortedMap of(final @Nullable IntComparator comparator, final int k0,
                                                 final short v0, final int k1, final short v1,
                                                 final int k2, final short v2, final int k3, final short v3,
                                                 final int k4, final short v4, final int k5, final short v5) {
        Int2ShortOpenCustomHashMap map = new Int2ShortOpenCustomHashMap(HashUtil.defaultIntStrategy());

        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);

        return of(map, comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2ShortSortedMap of(final @Nullable IntComparator comparator, final int @NotNull [] keys,
                                                 final short @NotNull ... values) {
        return of(new Int2ShortOpenCustomHashMap(keys, values, HashUtil.defaultIntStrategy()),
                comparator, MapType.memoryPriority());
    }

    static ImmutableInt2ShortSortedMap of(final Int2ShortOpenCustomHashMap values,
                                          final @Nullable IntComparator comparator,
                                          final @NotNull Comparator<? super MapType> priority) {
        if (values.isEmpty()) {
            return of(comparator);
        }

        if (values.size() == 1) {
            return of(comparator, values.keySet().iterator().nextInt(), values.values().iterator().nextShort());
        }

        final int min = values.keySet().intStream().min().getAsInt();
        final int max = values.keySet().intStream().max().getAsInt();
        final int range = max - min + 1;
        final double fragmentation = (double) range / values.size();
        final EnumSet<MapType> types = EnumSet.allOf(MapType.class);

        types.removeIf(t -> t.memoryEffectiveFragmentation() < fragmentation);

        switch (types.stream().max(priority).get()) {
            case HASH -> {
                return hashMap(comparator, HashUtil.defaultIntStrategy(), values.int2ShortEntrySet());
            }

            case TABLE -> {
                return comparator == null ? new ImmutableInt2ShortTableDefaultSortedMap(values,
                        new ImmutableIntTableSortedSet(IntArrays.EMPTY_ARRAY, BooleanArrays.EMPTY_ARRAY, 0,
                                null))
                        : new ImmutableInt2ShortTableSortedMap(comparator, values,
                        new ImmutableIntTableSortedSet(IntArrays.EMPTY_ARRAY, BooleanArrays.EMPTY_ARRAY, 0,
                                comparator));
            }

            case TABLE_FORK -> {
                return forkTableMap(comparator, HashUtil.defaultIntStrategy(), values.int2ShortEntrySet());
            }
        }

        throw new IllegalStateException();
    }

    static ImmutableInt2ShortHashSortedMap hashMap(final @Nullable IntComparator comparator,
                                                          @Nullable IntHash.Strategy strategy,
                                                          final @NotNull Int2ShortOpenCustomHashMap map) {
        return new ImmutableInt2ShortHashSortedMap(map, comparator);
    }

    static ImmutableInt2ShortForkTableSortedMap forkTableMap(final @Nullable IntComparator comparator,
                                                                    @Nullable IntHash.Strategy strategy,
                                                             final @NotNull Int2ShortOpenCustomHashMap map) {
        return comparator == null ? new ImmutableInt2ShortForkTableDefaultSortedMap(map, strategy)
                : new ImmutableInt2ShortForkTableSortedMap(map, strategy, comparator);
    }
}
