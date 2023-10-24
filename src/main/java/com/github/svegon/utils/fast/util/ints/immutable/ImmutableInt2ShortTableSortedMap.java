package com.github.svegon.utils.fast.util.ints.immutable;

import com.github.svegon.utils.collections.*;
import com.github.svegon.utils.fast.util.shorts.ShortMultiset;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Immutable
public class ImmutableInt2ShortTableSortedMap extends ImmutableInt2ShortSortedMap {
    final short[] table;
    private final AbstractImmutableIntTableSortedSet keySet;

    ImmutableInt2ShortTableSortedMap(int hashCode, short[] table, AbstractImmutableIntTableSortedSet keySet) {
        super(hashCode);
        this.table = table;
        this.keySet = keySet;
    }

    ImmutableInt2ShortTableSortedMap(int hashCode, AbstractImmutableIntTableSortedSet keySet) {
        this(hashCode, new short[keySet.elements.length], keySet);
    }

    ImmutableInt2ShortTableSortedMap(@Nullable IntComparator comparator, Int2ShortOpenCustomHashMap values,
                                     AbstractImmutableIntTableSortedSet prototype) {
        this(values.hashCode(), (AbstractImmutableIntTableSortedSet)
                ImmutableIntSortedSet.of(values.keySet().toIntArray(), comparator, prototype));

        IntArrays.unstableSort(keySet.elements, comparator);

        for (Entry e : values.int2ShortEntrySet()) {
            table[e.getIntKey() - keySet.offset] = e.getShortValue();
        }
    }

    @Override
    public int size() {
        return keySet.size();
    }

    @Override
    public ShortMultiset values() {
        return CollectionUtil.mapToShort(keySet, i -> table[i - keySet.offset]);
    }

    @Override
    public short get(int key) {
        return getOrDefault(key, defaultReturnValue());
    }

    @Override
    public boolean containsKey(int key) {
        return keySet.contains(key);
    }

    @Override
    public boolean containsValue(short value) {
        return values().contains(value);
    }

    @Override
    public short getOrDefault(int key, short defaultValue) {
        return keySet.contains(key) ? defaultValue : table[key - keySet.offset];
    }

    @Override
    public ObjectSortedSet<Entry> int2ShortEntrySet() {
        return SetUtil.mapToObj(keySet, (i) -> MapUtil.immutableEntry(i, table[i - keySet.offset]), Entry::getIntKey);
    }

    @Override
    public ImmutableIntSortedSet keySet() {
        return keySet;
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
            short v = table[i - keySet.offset];

            h += Integer.hashCode(i) ^ Short.hashCode(v);
        }

        return rangeCopy(h, table, (AbstractImmutableIntTableSortedSet)
                ImmutableIntSortedSet.of(elements, comparator(), keySet));
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
            short v = table[i - keySet.offset];

            h += Integer.hashCode(i) ^ Short.hashCode(v);
        }

        return rangeCopy(h, table, (AbstractImmutableIntTableSortedSet)
                ImmutableIntSortedSet.of(elements, comparator(), keySet));
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
            short v = table[i - keySet.offset];

            h += Integer.hashCode(i) ^ Short.hashCode(v);
        }

        return rangeCopy(h, table, (AbstractImmutableIntTableSortedSet)
                ImmutableIntSortedSet.of(elements, comparator(), keySet));
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
    public IntComparator comparator() {
        return keySet.comparator();
    }

    public int compare(int a, int b) {
        return comparator().compare(a, b);
    }

    protected ImmutableInt2ShortTableSortedMap rangeCopy(int hashCode, short[] table,
                                                         AbstractImmutableIntTableSortedSet keySet) {
        return new ImmutableInt2ShortTableSortedMap(hashCode, table, keySet);
    }
}
