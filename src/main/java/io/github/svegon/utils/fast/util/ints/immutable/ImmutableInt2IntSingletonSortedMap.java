package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.MultisetUtil;
import io.github.svegon.utils.fast.util.ints.IntMultiset;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import net.jcip.annotations.Immutable;

import java.util.Map;

@Immutable
public final class ImmutableInt2IntSingletonSortedMap extends ImmutableInt2IntSortedMap implements Int2IntMap.Entry {
    private final ImmutableIntSingletonSortedSet keySet;
    private final int value;

    ImmutableInt2IntSingletonSortedMap(ImmutableIntSingletonSortedSet keySet, int value) {
        super(Integer.hashCode(keySet.firstInt()) ^ Integer.hashCode(value));
        this.keySet = keySet;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        if (!(obj instanceof Map.Entry<?, ?> entry)) {
            return false;
        }

        return entry instanceof Entry e
                ? getIntKey() == e.getIntKey() && getIntValue() == e.getIntValue()
                : getKey().equals(entry.getKey()) && getValue().equals(entry.getValue());
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public IntMultiset values() {
        return MultisetUtil.singleton(value);
    }

    @Override
    public int get(int key) {
        return key == firstIntKey() ? value : defaultReturnValue();
    }

    @Override
    public boolean containsKey(int key) {
        return key == firstIntKey();
    }

    @Override
    public boolean containsValue(int value) {
        return this.value == value;
    }

    @Override
    public int getOrDefault(int key, int defaultValue) {
        return key == firstIntKey() ? value : defaultValue;
    }

    @Override
    public ObjectSortedSet<Entry> int2IntEntrySet() {
        return ObjectSortedSets.singleton(this);
    }

    @Override
    public ImmutableIntSortedSet keySet() {
        return keySet;
    }

    @Override
    public ImmutableInt2IntSortedMap subMap(int fromKey, int toKey) {
        int c = keySet.compare(fromKey, toKey);

        if (c > 0) {
            throw new IllegalArgumentException();
        }

        if (c == 0 || keySet.compare(firstIntKey(), fromKey) < 0) {
            return of(comparator());
        }

        return this;
    }

    @Override
    public ImmutableInt2IntSortedMap headMap(int toKey) {
        return keySet.compare(lastIntKey(), toKey) < 0 ? this : of(comparator());
    }

    @Override
    public ImmutableInt2IntSortedMap tailMap(int fromKey) {
        return keySet.compare(firstIntKey(), fromKey) < 0 ? of(comparator()) : this;
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

    @Override
    public int getIntKey() {
        return firstIntKey();
    }

    @Override
    public int getIntValue() {
        return value;
    }

    @Override
    public int setValue(int value) {
        throw new UnsupportedOperationException();
    }
}
