package com.github.svegon.utils.fast.util.ints.immutable;

import com.github.svegon.utils.ComparingUtil;
import com.github.svegon.utils.collections.MapType;
import com.github.svegon.utils.collections.SetUtil;
import com.github.svegon.utils.fast.util.ints.IntMultiset;
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
public abstract class ImmutableInt2IntSortedMap extends ImmutableInt2IntMap implements Int2IntSortedMap,
        IntComparator {
    static final MapType[] MAP_TYPES = MapType.values();

    ImmutableInt2IntSortedMap(int hashCode) {
        super(hashCode);
    }

    @Override
    public final ImmutableInt2IntSortedMap clone() {
        return this;
    }

    @Override
    public abstract ObjectSortedSet<Entry> int2IntEntrySet();

    @Override
    public abstract ImmutableIntSortedSet keySet();

    @Override
    public abstract IntMultiset values();

    @Override
    public abstract ImmutableInt2IntSortedMap subMap(int fromKey, int toKey);

    @Override
    public abstract ImmutableInt2IntSortedMap headMap(int toKey);

    @Override
    public abstract ImmutableInt2IntSortedMap tailMap(int fromKey);

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

    public static ImmutableInt2IntHashSortedMap hashMap(final @Nullable IntComparator comparator,
                                                          @Nullable IntHash.Strategy strategy,
                                                          final @NotNull Iterable<Entry> entries) {
        if (strategy == null) {
            strategy = HashUtil.defaultIntStrategy();
        }

        Int2IntOpenCustomHashMap map = new Int2IntOpenCustomHashMap(strategy);

        if (entries instanceof FastEntrySet) {
            ((FastEntrySet) entries).fastForEach(e -> map.put(e.getIntKey(), e.getIntValue()));
        } else {
            entries.forEach(e -> map.put(e.getIntKey(), e.getIntValue()));
        }

        return new ImmutableInt2IntHashSortedMap(map, comparator);
    }

    public static ImmutableInt2IntForkTableSortedMap forkTableMap(final @Nullable IntComparator comparator,
                                                              @Nullable IntHash.Strategy strategy,
                                                              final @NotNull Iterable<Entry> entries) {
        if (strategy == null) {
            strategy = HashUtil.defaultIntStrategy();
        }

        Int2IntOpenCustomHashMap map = new Int2IntOpenCustomHashMap(strategy);

        if (entries instanceof FastEntrySet) {
            ((FastEntrySet) entries).fastForEach(e -> map.put(e.getIntKey(), e.getIntValue()));
        } else {
            entries.forEach(e -> map.put(e.getIntKey(), e.getIntValue()));
        }

        return comparator == null ? new ImmutableInt2IntForkTableDefaultSortedMap(map, strategy)
                : new ImmutableInt2IntForkTableSortedMap(map, strategy, comparator);
    }

    public static ImmutableInt2IntSortedMap copyOf(final @NotNull Int2IntMap map,
                                                   final @Nullable IntComparator comparator,
                                                   final @Nullable IntHash.Strategy strategy) {
        Int2IntOpenCustomHashMap values = new Int2IntOpenCustomHashMap(map, HashUtil.defaultIntStrategy());

        if (values.isEmpty()) {
            return of(comparator);
        }

        return SetUtil.fragmentation(values.keySet()) < MapType.HASH.memoryEffectiveFragmentation()
                ? hashMap(comparator, values) : forkTableMap(comparator, strategy, values);
    }

    public static ImmutableInt2IntSortedMap copyOf(final @Nullable IntComparator comparator,
                                                   final @NotNull Int2IntMap map) {
        return of(new Int2IntOpenCustomHashMap(map, HashUtil.defaultIntStrategy())
                , comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2IntSortedMap copyOf(final @NotNull Int2IntSortedMap map) {
        if (map instanceof ImmutableInt2IntSortedMap) {
            return (ImmutableInt2IntSortedMap) map;
        }

        return copyOf(map.comparator(), map);
    }

    public static ImmutableInt2IntSortedMap copyOf(final @Nullable IntComparator comparator,
                                                   final @NotNull Iterable<Entry> entries) {
        Int2IntOpenCustomHashMap values = new Int2IntOpenCustomHashMap(HashUtil.defaultIntStrategy());

        for (Entry entry : entries) {
            values.put(entry.getIntKey(), entry.getIntValue());
        }

        return of(values, comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2IntEmptySortedMap of(final @Nullable IntComparator comparator) {
        return comparator == null ? ImmutableInt2IntEmptySortedMap.DEFAULT_SORTED
                : new ImmutableInt2IntEmptySortedMap(comparator);
    }

    public static ImmutableInt2IntSingletonSortedMap of(final @Nullable IntComparator comparator, final int key,
                                                           final int value) {
        return new ImmutableInt2IntSingletonSortedMap(ImmutableIntSortedSet.of(comparator, key), value);
    }

    public static ImmutableInt2IntSortedMap of(final @Nullable IntComparator comparator, final int k0,
                                               final int v0, final int k1, final int v1) {
        Int2IntOpenCustomHashMap map = new Int2IntOpenCustomHashMap(HashUtil.defaultIntStrategy());

        map.put(k0, v0);
        map.put(k1, v1);

        return of(map, comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2IntSortedMap of(final @Nullable IntComparator comparator, final int k0,
                                               final int v0, final int k1, final int v1,
                                               final int k2, final int v2) {
        Int2IntOpenCustomHashMap map = new Int2IntOpenCustomHashMap(HashUtil.defaultIntStrategy());

        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);

        return of(map, comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2IntSortedMap of(final @Nullable IntComparator comparator, final int k0,
                                               final int v0, final int k1, final int v1,
                                               final int k2, final int v2, final int k3, final int v3) {
        Int2IntOpenCustomHashMap map = new Int2IntOpenCustomHashMap(HashUtil.defaultIntStrategy());

        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);

        return of(map, comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2IntSortedMap of(final @Nullable IntComparator comparator, final int k0,
                                               final int v0, final int k1, final int v1,
                                               final int k2, final int v2, final int k3, final int v3,
                                               final int k4, final int v4) {
        Int2IntOpenCustomHashMap map = new Int2IntOpenCustomHashMap(HashUtil.defaultIntStrategy());

        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);

        return of(map, comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2IntSortedMap of(final @Nullable IntComparator comparator, final int k0,
                                               final int v0, final int k1, final int v1,
                                               final int k2, final int v2, final int k3, final int v3,
                                               final int k4, final int v4, final int k5, final int v5) {
        Int2IntOpenCustomHashMap map = new Int2IntOpenCustomHashMap(HashUtil.defaultIntStrategy());

        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);

        return of(map, comparator, MapType.memoryPriority());
    }

    public static ImmutableInt2IntSortedMap of(final @Nullable IntComparator comparator,
                                               final int @NotNull ... keyValuePairs) {
        return of(comparator, MapType.memoryPriority(), keyValuePairs);
    }

    public static ImmutableInt2IntSortedMap of(final @Nullable IntComparator comparator,
                                               final @NotNull Comparator<? super MapType> priority,
                                               final int @NotNull ... keyValuePairs) {
        if ((keyValuePairs.length & 1) != 0) {
            throw new IllegalArgumentException("unpaired key value pairs");
        }

        Int2IntOpenCustomHashMap map = new Int2IntOpenCustomHashMap(HashUtil.defaultIntStrategy());

        for (int i = 0; i < keyValuePairs.length; i += 2) {
            map.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }

        return of(map, comparator, priority);
    }

    static ImmutableInt2IntSortedMap of(final Int2IntOpenCustomHashMap values,
                                        final @Nullable IntComparator comparator,
                                        final @NotNull Comparator<? super MapType> priority) {
        if (values.isEmpty()) {
            return of(comparator);
        }

        if (values.size() == 1) {
            return of(comparator, values.keySet().iterator().nextInt(), values.values().iterator().nextInt());
        }

        final int min = values.keySet().intStream().min().getAsInt();
        final int max = values.keySet().intStream().max().getAsInt();
        final int range = max - min + 1;
        final double fragmentation = (double) range / values.size();
        final EnumSet<MapType> types = EnumSet.allOf(MapType.class);

        types.removeIf(t -> t.memoryEffectiveFragmentation() < fragmentation);

        switch (types.stream().max(priority).get()) {
            case HASH -> {
                return hashMap(comparator, HashUtil.defaultIntStrategy(), values.int2IntEntrySet());
            }

            case TABLE -> {
                return comparator == null ? new ImmutableInt2IntTableDefaultSortedMap(values,
                        new ImmutableIntTableSortedSet(IntArrays.EMPTY_ARRAY, BooleanArrays.EMPTY_ARRAY, 0,
                                null))
                        : new ImmutableInt2IntTableSortedMap(comparator, values,
                        new ImmutableIntTableSortedSet(IntArrays.EMPTY_ARRAY, BooleanArrays.EMPTY_ARRAY, 0,
                                comparator));
            }

            case TABLE_FORK -> {
                return forkTableMap(comparator, HashUtil.defaultIntStrategy(), values.int2IntEntrySet());
            }
        }

        throw new IllegalStateException();
    }

    static ImmutableInt2IntHashSortedMap hashMap(final @Nullable IntComparator comparator,
                                                 final @NotNull Int2IntOpenCustomHashMap map) {
        return new ImmutableInt2IntHashSortedMap(map, comparator);
    }

    static ImmutableInt2IntForkTableSortedMap forkTableMap(final @Nullable IntComparator comparator,
                                                                    @Nullable IntHash.Strategy strategy,
                                                             final @NotNull Int2IntOpenCustomHashMap map) {
        return comparator == null ? new ImmutableInt2IntForkTableDefaultSortedMap(map, strategy)
                : new ImmutableInt2IntForkTableSortedMap(map, strategy, comparator);
    }
}
