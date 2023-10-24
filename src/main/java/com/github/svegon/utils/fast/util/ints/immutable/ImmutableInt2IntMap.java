package com.github.svegon.utils.fast.util.ints.immutable;

import com.github.svegon.utils.collections.MapType;
import com.github.svegon.utils.collections.MapUtil;
import com.github.svegon.utils.fast.util.ints.IntMultiset;
import com.github.svegon.utils.hash.HashUtil;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.ints.IntBinaryOperator;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.function.BiFunction;

@Immutable
public abstract class ImmutableInt2IntMap implements Int2IntMap {
    private final int hashCode;

    ImmutableInt2IntMap(int hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public final int hashCode() {
        return hashCode;
    }

    @Override
    public ImmutableInt2IntMap clone() {
        return this;
    }

    @Override
    public abstract int size();

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public final void putAll(@NotNull Map<? extends Integer, ? extends Integer> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void defaultReturnValue(int rv) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int defaultReturnValue() {
        return 0;
    }

    @Override
    public abstract ObjectSet<Entry> int2IntEntrySet();

    @Override
    public abstract ImmutableIntSet keySet();

    @Override
    public abstract IntMultiset values();

    @Override
    public int get(int key) {
        return getOrDefault(key, defaultReturnValue());
    }

    @Override
    public abstract boolean containsKey(int key);

    @Override
    public abstract boolean containsValue(int value);

    @Override
    public abstract int getOrDefault(int key, int defaultValue);

    @Override
    public final int putIfAbsent(int key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean remove(int key, int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean replace(int key, int oldValue, int newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int computeIfAbsent(int key, Int2IntFunction mappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int compute(int key, BiFunction<? super Integer, ? super Integer,
            ? extends Integer> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int mergeInt(int key, int value, IntBinaryOperator remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int computeIfPresent(int key,
                                        BiFunction<? super Integer, ? super Integer,
                                                ? extends Integer> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    public static ImmutableInt2IntHashMap hashMap(@Nullable IntHash.Strategy strategy,
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

        return new ImmutableInt2IntHashMap(map);
    }

    public static ImmutableInt2IntForkTableMap forkTableMap(@Nullable IntHash.Strategy strategy,
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

        return new ImmutableInt2IntForkTableMap(map);
    }

    public static ImmutableInt2IntMap copyOf(final @NotNull Int2IntMap map,
                                             final @NotNull Comparator<? super MapType> priority) {
        if (map instanceof ImmutableInt2IntMap) {
            return (ImmutableInt2IntMap) map;
        }

        return copyOf(map.int2IntEntrySet(), priority);
    }

    public static ImmutableInt2IntMap copyOf(final @NotNull Int2IntMap map) {
        if (map instanceof ImmutableInt2IntMap) {
            return (ImmutableInt2IntMap) map;
        }

        return ImmutableInt2IntSortedMap.copyOf(null, map);
    }

    public static ImmutableInt2IntMap copyOf(final @NotNull Iterable<Entry> entries,
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

        return ImmutableInt2IntSortedMap.copyOf(null, entries);
    }

    public static ImmutableInt2IntSortedMap copyOf(final @NotNull Iterable<Entry> entries) {
        return ImmutableInt2IntSortedMap.copyOf(null, entries);
    }

    public static ImmutableInt2IntEmptySortedMap of() {
        return ImmutableInt2IntEmptySortedMap.DEFAULT_SORTED;
    }

    public static ImmutableInt2IntSingletonSortedMap of(final int key, final int value) {
        return ImmutableInt2IntSortedMap.of(null, key, value);
    }

    public static ImmutableInt2IntSortedMap of(final @NotNull Comparator<? super MapType> priority,
                                                 final int @NotNull ... values) {
        return ImmutableInt2IntSortedMap.of(null, priority, values);
    }

    public static ImmutableInt2IntSortedMap of(final int @NotNull ... keyValuePairs) {
        return of(MapType.memoryPriority(), keyValuePairs);
    }
}
