package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.CollectionUtil;
import io.github.svegon.utils.collections.MapUtil;
import io.github.svegon.utils.collections.SetUtil;
import io.github.svegon.utils.fast.util.ints.IntMultiset;
import it.unimi.dsi.fastutil.ints.Int2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Immutable
public class ImmutableInt2IntTableSortedMap extends ImmutableInt2IntSortedMap {
    final int[] table;
    private final AbstractImmutableIntTableSortedSet keySet;

    ImmutableInt2IntTableSortedMap(int hashCode, int[] table, AbstractImmutableIntTableSortedSet keySet) {
        super(hashCode);
        this.table = table;
        this.keySet = keySet;
    }

    ImmutableInt2IntTableSortedMap(int hashCode, AbstractImmutableIntTableSortedSet keySet) {
        this(hashCode, new int[keySet.elements.length], keySet);
    }

    ImmutableInt2IntTableSortedMap(@Nullable IntComparator comparator, Int2IntOpenCustomHashMap values,
                                   AbstractImmutableIntTableSortedSet prototype) {
        this(values.hashCode(), (AbstractImmutableIntTableSortedSet)
                ImmutableIntSortedSet.of(values.keySet().toIntArray(), comparator, prototype));

        IntArrays.unstableSort(keySet.elements, comparator);

        for (Entry e : values.int2IntEntrySet()) {
            table[e.getIntKey() - keySet.offset] = e.getIntValue();
        }
    }

    @Override
    public int size() {
        return keySet.size();
    }

    @Override
    public IntMultiset values() {
        return CollectionUtil.mapToInt(keySet, i -> table[i - keySet.offset]);
    }

    @Override
    public int get(int key) {
        return getOrDefault(key, defaultReturnValue());
    }

    @Override
    public boolean containsKey(int key) {
        return keySet.contains(key);
    }

    @Override
    public boolean containsValue(int value) {
        return values().contains(value);
    }

    @Override
    public int getOrDefault(int key, int defaultValue) {
        return keySet.contains(key) ? defaultValue : table[key - keySet.offset];
    }

    @Override
    public ObjectSortedSet<Entry> int2IntEntrySet() {
        return SetUtil.mapToObj(keySet, (i) -> MapUtil.immutableEntry(i, table[i - keySet.offset]), Entry::getIntKey);
    }

    @Override
    public ImmutableIntSortedSet keySet() {
        return keySet;
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
            int v = table[i - keySet.offset];

            h += Integer.hashCode(i) ^ Integer.hashCode(v);
        }

        return rangeCopy(h, table, (AbstractImmutableIntTableSortedSet)
                ImmutableIntSortedSet.of(elements, comparator(), keySet));
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
            int v = table[i - keySet.offset];

            h += Integer.hashCode(i) ^ Integer.hashCode(v);
        }

        return rangeCopy(h, table, (AbstractImmutableIntTableSortedSet)
                ImmutableIntSortedSet.of(elements, comparator(), keySet));
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
            int v = table[i - keySet.offset];

            h += Integer.hashCode(i) ^ Integer.hashCode(v);
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

    protected ImmutableInt2IntTableSortedMap rangeCopy(int hashCode, int[] table,
                                                       AbstractImmutableIntTableSortedSet keySet) {
        return new ImmutableInt2IntTableSortedMap(hashCode, table, keySet);
    }
}
