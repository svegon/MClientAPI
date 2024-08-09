package io.github.svegon.utils.fast.util.bytes.immutable;

import it.unimi.dsi.fastutil.bytes.Byte2ShortMap;
import it.unimi.dsi.fastutil.bytes.Byte2ShortSortedMap;
import it.unimi.dsi.fastutil.bytes.ByteComparator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import it.unimi.dsi.fastutil.shorts.ShortSets;
import net.jcip.annotations.Immutable;

import java.util.Map;

@Immutable
public final class ImmutableByte2ShortSingletonSortedMap extends ImmutableByte2ShortSortedMap
        implements Byte2ShortMap.Entry {
    private final ImmutableByteSingletonSortedSet keySet;
    private final short value;

    ImmutableByte2ShortSingletonSortedMap(ImmutableByteSingletonSortedSet keySet, short value) {
        super(Byte.hashCode(keySet.firstByte()) ^ Short.hashCode(value));
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
                ? getByteKey() == e.getByteKey() && getShortValue() == e.getShortValue()
                : getKey().equals(entry.getKey()) && getValue().equals(entry.getValue());
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public ShortSet values() {
        return ShortSets.singleton(value);
    }

    @Override
    public short get(byte key) {
        return key == firstByteKey() ? value : defaultReturnValue();
    }

    @Override
    public boolean containsKey(byte key) {
        return key == firstByteKey();
    }

    @Override
    public boolean containsValue(short value) {
        return this.value == value;
    }

    @Override
    public short getOrDefault(byte key, short defaultValue) {
        return key == firstByteKey() ? value : defaultValue;
    }

    @Override
    public ObjectSortedSet<Entry> byte2ShortEntrySet() {
        return ObjectSortedSets.singleton(this);
    }

    @Override
    public ImmutableByteSortedSet keySet() {
        return keySet;
    }

    @Override
    public ImmutableByte2ShortSortedMap subMap(byte fromKey, byte toKey) {
        int c = keySet.compare(fromKey, toKey);

        if (c > 0) {
            throw new IllegalArgumentException();
        }

        if (c == 0 || keySet.compare(firstByteKey(), fromKey) < 0) {
            return of(comparator());
        }

        return this;
    }

    @Override
    public Byte2ShortSortedMap tailMap(byte fromKey) {
        return keySet.compare(firstByteKey(), fromKey) < 0 ? of(comparator()) : this;
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
    public short getShortValue() {
        return value;
    }

    @Override
    public short setValue(short value) {
        throw new UnsupportedOperationException();
    }
}
