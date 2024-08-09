package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.MultisetUtil;
import io.github.svegon.utils.collections.SetType;
import io.github.svegon.utils.collections.SetUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.shorts.AbstractShortMultiset;
import io.github.svegon.utils.fast.util.shorts.ShortMultiset;
import io.github.svegon.utils.hash.HashUtil;
import io.github.svegon.utils.hash.IntForkTable;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.ints.Int2ShortMap;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntHash;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.shorts.Short2IntOpenHashMap;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Immutable
public class ImmutableInt2ShortForkTableSortedMap extends ImmutableInt2ShortSortedMap implements Size64 {
    private final IntForkTable<Short, ImmutableInt2ShortForkTableMap.Entry> table;
    private final KeySet keySet;
    private final ValuesMultiset values;

    ImmutableInt2ShortForkTableSortedMap(final int hashCode, final int @NotNull [] keys,
                                         final @NotNull ImmutableInt2ShortForkTableSortedMap original) {
        super(hashCode);
        this.table = new IntForkTable<>(original.table.getStrategy());

        for (int i : keys) {
            table.put(original.table.get(i));
        }

        this.keySet = new KeySet(keys, original.comparator(), original.table.getStrategy());
        this.values = new ValuesMultiset();
    }

    ImmutableInt2ShortForkTableSortedMap(final @NotNull Int2ShortMap map, @Nullable IntHash.Strategy strategy,
                                         final @Nullable IntComparator comparator) {
        super(hash(map, strategy = (strategy == null ? HashUtil.defaultIntStrategy() : strategy)));
        this.table = new IntForkTable<>(strategy);
        int[] keys = new int[map.size()];
        int i = 0;

        for (Entry e : map.int2ShortEntrySet()) {
            ImmutableInt2ShortForkTableMap.Entry entry = new ImmutableInt2ShortForkTableMap.Entry(table,
                    e.getIntKey(), e.getShortValue());
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
    public ObjectSortedSet<Entry> int2ShortEntrySet() {
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
    public ShortMultiset values() {
        return values;
    }

    @Override
    public ImmutableInt2ShortSortedMap subMap(int fromKey, int toKey) {
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
            short v = table.get(i).getShortValue();

            h += table.getStrategy().hashCode(i) ^ Short.hashCode(v);
        }

        return rangeCopy(h, elements, this);
    }

    @Override
    public ImmutableInt2ShortSortedMap headMap(int toKey) {
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
            short v = table.get(i).getShortValue();

            h += table.getStrategy().hashCode(i) ^ Short.hashCode(v);
        }

        return rangeCopy(h, elements, this);
    }

    @Override
    public ImmutableInt2ShortSortedMap tailMap(int fromKey) {
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
            short v = table.get(i).getShortValue();

            h += table.getStrategy().hashCode(i) ^ Short.hashCode(v);
        }

        return rangeCopy(h, elements, this);
    }

    @Override
    public boolean containsKey(int key) {
        return table.containsKey(key);
    }

    @Override
    public boolean containsValue(short value) {
        return values().contains(value);
    }

    @Override
    public short getOrDefault(int key, short defaultValue) {
        ImmutableInt2ShortForkTableMap.Entry e = getEntry(key);
        return e == null ? defaultValue : e.getShortValue();
    }

    @Override
    public int compare(int k1, int k2) {
        return comparator().compare(k1, k2);
    }

    public ImmutableInt2ShortForkTableMap.Entry getEntry(int key) {
        return table.get(key);
    }

    protected ImmutableInt2ShortForkTableSortedMap rangeCopy(int hashCode, int[] keys,
                                                             ImmutableInt2ShortForkTableSortedMap original) {
        return new ImmutableInt2ShortForkTableSortedMap(hashCode, keys, original);
    }

    private static int hash(Int2ShortMap map, IntHash.Strategy strategy) {
        int h = 0;

        ObjectSet<Entry> entrySet = map.int2ShortEntrySet();
        ObjectIterator<Entry> it = entrySet instanceof FastEntrySet
                ? ((FastEntrySet) entrySet).fastIterator() : entrySet.iterator();

        while (it.hasNext()) {
            Entry e = it.next();
            h += strategy.hashCode(e.getIntKey()) ^ Short.hashCode(e.getShortValue());
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
            return ImmutableInt2ShortForkTableSortedMap.this.containsKey(key);
        }
    }

    @Immutable
    private final class ValuesMultiset extends AbstractShortMultiset implements Size64 {
        private ObjectSet<ShortMultiset.Entry> shortEntrySet;

        @Override
        public ShortIterator iterator() {
            return IterationUtil.mapToShort(keySet.iterator(), ImmutableInt2ShortForkTableSortedMap.this);
        }

        @Deprecated
        @Override
        @SuppressWarnings("deprecation")
        public int size() {
            return Size64.super.size();
        }

        @Override
        public long size64() {
            return ImmutableInt2ShortForkTableSortedMap.this.size64();
        }

        @Override
        public ObjectSet<ShortMultiset.Entry> shortEntrySet() {
            if (shortEntrySet == null) {
                Short2IntOpenHashMap entries = new Short2IntOpenHashMap(size());

                for (short s : this) {
                    entries.addTo(s, 1);
                }

                final ObjectOpenHashSet<ShortMultiset.Entry> entrySet = new ObjectOpenHashSet<>();
                entries.short2IntEntrySet().fastForEach(e -> entrySet.add(MultisetUtil.immutableEntry(e.getShortKey(),
                        e.getIntValue())));
                shortEntrySet = entrySet;
            }

            return shortEntrySet;
        }
    }
}
