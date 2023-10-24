package com.github.svegon.utils.fast.util.bytes.immutable;

import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import net.jcip.annotations.Immutable;

import java.util.Map;

@Immutable
public final class ImmutableByte2ByteSingletonSortedMap extends ImmutableByte2ByteSortedMap
        implements Byte2ByteMap.Entry {
    private final ImmutableByteSingletonSortedSet keySet;
    private final byte value;

    ImmutableByte2ByteSingletonSortedMap(ImmutableByteSingletonSortedSet keySet, byte value) {
        super(Byte.hashCode(keySet.firstByte()) ^ Byte.hashCode(value));
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
                ? getByteKey() == e.getByteKey() && getByteValue() == e.getByteValue()
                : getKey().equals(entry.getKey()) && getValue().equals(entry.getValue());
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public ByteSet values() {
        return ByteSortedSets.singleton(value);
    }

    @Override
    public byte get(byte key) {
        return key == firstByteKey() ? value : defaultReturnValue();
    }

    @Override
    public boolean containsKey(byte key) {
        return key == firstByteKey();
    }

    @Override
    public boolean containsValue(byte value) {
        return this.value == value;
    }

    @Override
    public byte getOrDefault(byte key, byte defaultValue) {
        return key == firstByteKey() ? value : defaultValue;
    }

    @Override
    public ObjectSortedSet<Entry> byte2ByteEntrySet() {
        return ObjectSortedSets.singleton(this);
    }

    @Override
    public ImmutableByteSortedSet keySet() {
        return keySet;
    }

    @Override
    public Byte2ByteSortedMap subMap(byte fromKey, byte toKey) {
        int c = keySet.compare(fromKey, toKey);

        if (c > 0) {
            throw new IllegalArgumentException();
        }

        if (c == 0 || keySet.compare(firstByteKey(), fromKey) < 0) {
            return ImmutableByte2ByteSortedMap.of(comparator());
        }

        return this;
    }

    @Override
    public Byte2ByteSortedMap tailMap(byte fromKey) {
        return keySet.compare(firstByteKey(), fromKey) < 0 ? ImmutableByte2ByteSortedMap.of(comparator()) : this;
    }

    @Override
    public byte firstByteKey() {
        return keySet.firstByte();
    }

    @Override
    public byte lastByteKey() {
        return keySet.lastByte();
    }

    @Override
    public ByteComparator comparator() {
        return keySet.comparator();
    }

    @Override
    public byte getByteKey() {
        return firstByteKey();
    }

    @Override
    public byte getByteValue() {
        return value;
    }

    @Override
    public byte setValue(byte value) {
        throw new UnsupportedOperationException();
    }
}
