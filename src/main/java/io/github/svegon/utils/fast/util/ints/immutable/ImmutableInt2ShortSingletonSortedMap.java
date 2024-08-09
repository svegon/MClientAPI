package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.MultisetUtil;
import io.github.svegon.utils.fast.util.shorts.ShortMultiset;
import it.unimi.dsi.fastutil.ints.Int2ShortMap;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import net.jcip.annotations.Immutable;

import java.util.Map;

@Immutable
public final class ImmutableInt2ShortSingletonSortedMap extends ImmutableInt2ShortSortedMap
        implements Int2ShortMap.Entry {
    private final ImmutableIntSingletonSortedSet keySet;
    private final short value;

    ImmutableInt2ShortSingletonSortedMap(ImmutableIntSingletonSortedSet keySet, short value) {
        super(Integer.hashCode(keySet.firstInt()) ^ Short.hashCode(value));
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
                ? getIntKey() == e.getIntKey() && getShortValue() == e.getShortValue()
                : getKey().equals(entry.getKey()) && getValue().equals(entry.getValue());
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public ShortMultiset values() {
        return MultisetUtil.singleton(value);
    }

    @Override
    public short get(int key) {
        return key == firstIntKey() ? value : defaultReturnValue();
    }

    @Override
    public boolean containsKey(int key) {
        return key == firstIntKey();
    }

    @Override
    public boolean containsValue(short value) {
        return this.value == value;
    }

    @Override
    public short getOrDefault(int key, short defaultValue) {
        return key == firstIntKey() ? value : defaultValue;
    }

    @Override
    public ObjectSortedSet<Entry> int2ShortEntrySet() {
        return ObjectSortedSets.singleton(this);
    }

    @Override
    public ImmutableIntSortedSet keySet() {
        return keySet;
    }

    @Override
    public ImmutableInt2ShortSortedMap subMap(int fromKey, int toKey) {
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
    public ImmutableInt2ShortSortedMap headMap(int toKey) {
        return keySet.compare(lastIntKey(), toKey) < 0 ? this : of(comparator());
    }

    @Override
    public ImmutableInt2ShortSortedMap tailMap(int fromKey) {
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
    public short getShortValue() {
        return value;
    }

    @Override
    public short setValue(short value) {
        throw new UnsupportedOperationException();
    }
}
