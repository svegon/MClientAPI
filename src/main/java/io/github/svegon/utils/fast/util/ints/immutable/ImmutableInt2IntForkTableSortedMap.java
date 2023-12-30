package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.MultisetUtil;
import io.github.svegon.utils.collections.SetType;
import io.github.svegon.utils.collections.SetUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.ints.AbstractIntMultiset;
import io.github.svegon.utils.fast.util.ints.IntMultiset;
import io.github.svegon.utils.hash.HashUtil;
import io.github.svegon.utils.hash.IntForkTable;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntHash;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Immutable
public class ImmutableInt2IntForkTableSortedMap extends ImmutableInt2IntSortedMap implements Size64 {
    private final IntForkTable<Integer, ImmutableInt2IntForkTableMap.Entry> table;
    private final KeySet keySet;
    private final ValuesMultiset values;

    ImmutableInt2IntForkTableSortedMap(final int hashCode, final int @NotNull [] keys,
                                       final @NotNull ImmutableInt2IntForkTableSortedMap original) {
        super(hashCode);
        this.table = new IntForkTable<>(original.table.getStrategy());

        for (int i : keys) {
            table.put(original.table.get(i));
        }

        this.keySet = new KeySet(keys, original.comparator(), original.table.getStrategy());
        this.values = new ValuesMultiset();
    }

    ImmutableInt2IntForkTableSortedMap(final @NotNull Int2IntMap map, @Nullable IntHash.Strategy strategy,
                                       final @Nullable IntComparator comparator) {
        super(hash(map, strategy = (strategy == null ? HashUtil.defaultIntStrategy() : strategy)));
        this.table = new IntForkTable<>(strategy);
        int[] keys = new int[map.size()];
        int i = 0;

        for (Entry e : map.int2IntEntrySet()) {
            ImmutableInt2IntForkTableMap.Entry entry = new ImmutableInt2IntForkTableMap.Entry(table,
                    e.getIntKey(), e.getIntValue());
            table.put(entry);
            keys[i++] = e.getIntKey();
        }

        IntArrays.unstableSort(keys, comparator);

        this.keySet = new KeySet(keys, comparator, strategy);
        this.values = new ValuesMultiset();
    }

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    public int size() {
        return table.size();
    }

    @Override
    public long size64() {
        return table.size64();
    }

    @Override
    public ObjectSortedSet<Entry> int2IntEntrySet() {
        return SetUtil.mapToObj(keySet, table::get, Entry::getIntKey);
    }

    @Override
    public ImmutableIntSortedSet keySet() {
        return keySet;
    }

    @Override
    public int firstIntKey() {
        return keySet.firstInt();
    }

    @Override
    public int lastIntKey() {
        return keySet.lastInt();
    }

    @Override
    public final IntComparator comparator() {
        return keySet.comparator;
    }

    @Override
    public IntMultiset values() {
        return values;
    }

    @Override
    public ImmutableInt2IntSortedMap subMap(int fromKey, int toKey) {
        if (compare(fromKey, toKey) > 0) {
            throw new IllegalArgumentException();
        }

        int from = 0;
        int to = keySet.elements.length;

        while (compare(keySet.elements[from], fromKey) < 0) {
            from++;

            if (from >= keySet.elements.length) {
                return of(comparator());
            }
        }

        while (compare(keySet.elements[--to], toKey) > 0) {
            if (to <= 0) {
                return of(comparator());
            }
        }

        if (from > ++to) {
            throw new IllegalArgumentException();
        }

        if (from == to) {
            return of(comparator());
        }

        if (from == 0 && to == keySet.elements.length) {
            return this;
        }

        int h = 0;
        int[] elements = Arrays.copyOfRange(keySet.elements, from, to);

        for (int i : elements) {
            int v = table.get(i).getIntValue();

            h += table.getStrategy().hashCode(i) ^ Integer.hashCode(v);
        }

        return rangeCopy(h, elements, this);
    }

    @Override
    public ImmutableInt2IntSortedMap headMap(int toKey) {
        int to = keySet.elements.length;

        while (compare(keySet.elements[--to], toKey) > 0) {
            if (to <= 0) {
                return of(comparator());
            }
        }

        if (++to == keySet.elements.length) {
            return this;
        }

        int h = 0;
        int[] elements = Arrays.copyOf(keySet.elements, to);

        for (int i : elements) {
            int v = table.get(i).getIntValue();

            h += table.getStrategy().hashCode(i) ^ Integer.hashCode(v);
        }

        return rangeCopy(h, elements, this);
    }

    @Override
    public ImmutableInt2IntSortedMap tailMap(int fromKey) {
        int from = 0;

        while (compare(keySet.elements[from], fromKey) < 0) {
            from++;

            if (from >= keySet.elements.length) {
                return of(comparator());
            }
        }

        int h = 0;
        int[] elements = Arrays.copyOfRange(keySet.elements, from, keySet.elements.length);

        for (int i : elements) {
            int v = table.get(i).getIntValue();

            h += table.getStrategy().hashCode(i) ^ Integer.hashCode(v);
        }

        return rangeCopy(h, elements, this);
    }

    @Override
    public boolean containsKey(int key) {
        return table.containsKey(key);
    }

    @Override
    public boolean containsValue(int value) {
        return values().contains(value);
    }

    @Override
    public int getOrDefault(int key, int defaultValue) {
        ImmutableInt2IntForkTableMap.Entry e = getEntry(key);
        return e == null ? defaultValue : e.getIntValue();
    }

    @Override
    public int compare(int k1, int k2) {
        return comparator().compare(k1, k2);
    }

    public ImmutableInt2IntForkTableMap.Entry getEntry(int key) {
        return table.get(key);
    }

    protected ImmutableInt2IntForkTableSortedMap rangeCopy(int hashCode, int[] keys,
                                                           ImmutableInt2IntForkTableSortedMap original) {
        return new ImmutableInt2IntForkTableSortedMap(hashCode, keys, original);
    }

    private static int hash(Int2IntMap map, IntHash.Strategy strategy) {
        int h = 0;

        ObjectSet<Entry> entrySet = map.int2IntEntrySet();
        ObjectIterator<Entry> it = entrySet instanceof FastEntrySet
                ? ((FastEntrySet) entrySet).fastIterator() : entrySet.iterator();

        while (it.hasNext()) {
            Entry e = it.next();
            h += strategy.hashCode(e.getIntKey()) ^ Integer.hashCode(e.getIntValue());
        }

        return h;
    }

    @Immutable
    private final class KeySet extends AbstractImmutableIntHashSortedSet {
        KeySet(int[] elements, @Nullable IntComparator comparator, IntHash.Strategy strategy) {
            super(elements, comparator, strategy);
        }

        @Override
        public ImmutableIntSortedSet toSortedSet(final @Nullable IntComparator comparator) {
            return copyOf(this, comparator, SetType.memoryPriority());
        }

        @Override
        public boolean contains(int key) {
            return ImmutableInt2IntForkTableSortedMap.this.containsKey(key);
        }
    }

    @Immutable
    private final class ValuesMultiset extends AbstractIntMultiset implements Size64 {
        private ObjectSet<IntMultiset.Entry> intEntrySet;

        @Override
        public IntIterator iterator() {
            return IterationUtil.mapToInt(keySet.iterator(), ImmutableInt2IntForkTableSortedMap.this);
        }

        @Deprecated
        @Override
        @SuppressWarnings("deprecation")
        public int size() {
            return Size64.super.size();
        }

        @Override
        public long size64() {
            return ImmutableInt2IntForkTableSortedMap.this.size64();
        }

        @Override
        public ObjectSet<IntMultiset.Entry> intEntrySet() {
            if (intEntrySet == null) {
                Int2IntOpenHashMap entries = new Int2IntOpenHashMap(size());

                for (int s : this) {
                    entries.addTo(s, 1);
                }

                final ObjectOpenHashSet<IntMultiset.Entry> entrySet = new ObjectOpenHashSet<>();
                entries.int2IntEntrySet().fastForEach(e -> entrySet.add(MultisetUtil.immutableEntry(e.getIntKey(),
                        e.getIntValue())));
                intEntrySet = entrySet;
            }

            return intEntrySet;
        }
    }
}
