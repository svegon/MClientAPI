package com.github.svegon.utils.fast.util.bytes.immutable;

import com.github.svegon.utils.collections.CollectionUtil;
import com.github.svegon.utils.collections.MapUtil;
import com.github.svegon.utils.collections.SetUtil;
import com.github.svegon.utils.fast.util.bytes.shorts.Byte2ShortTableMap;
import com.github.svegon.utils.fast.util.shorts.ShortMultiset;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.bytes.ByteComparator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Immutable
public class ImmutableByte2ShortTableSortedMap extends ImmutableByte2ShortSortedMap {
    private final short[] table;
    private final ImmutableByteTableSortedSet keySet;

    ImmutableByte2ShortTableSortedMap(int hashCode, short[] table, ImmutableByteTableSortedSet keySet) {
        super(hashCode);
        this.table = table;
        this.keySet = keySet;
    }

    ImmutableByte2ShortTableSortedMap(int hashCode, ImmutableByteTableSortedSet keySet) {
        this(hashCode, new short[256], keySet);
    }

    ImmutableByte2ShortTableSortedMap(@Nullable ByteComparator comparator, Byte2ShortTableMap values) {
        this(values.hashCode(), new ImmutableByteTableSortedSet(values.keySet().toByteArray(), comparator));

        ByteArrays.unstableSort(keySet.elements, comparator);

        for (Entry e : values.byte2ShortEntrySet()) {
            table[Byte.toUnsignedInt(e.getByteKey())] = e.getShortValue();
        }
    }

    @Override
    public int size() {
        return keySet.size();
    }

    @Override
    public ShortMultiset values() {
        return CollectionUtil.mapToShort(keySet, b -> table[Byte.toUnsignedInt(b)]);
    }

    @Override
    public short get(byte key) {
        return getOrDefault(key, defaultReturnValue());
    }

    @Override
    public boolean containsKey(byte key) {
        return keySet.contains(key);
    }

    @Override
    public boolean containsValue(short value) {
        return values().contains(value);
    }

    @Override
    public short getOrDefault(byte key, short defaultValue) {
        return keySet.contains(key) ? defaultValue : table[Byte.toUnsignedInt(key)];
    }

    @Override
    public ObjectSortedSet<Entry> byte2ShortEntrySet() {
        return SetUtil.mapToObj(keySet, (b) -> MapUtil.immutableEntry(b, table[Byte.toUnsignedInt(b)]),
                Entry::getByteKey);
    }

    @Override
    public ImmutableByteSortedSet keySet() {
        return keySet;
    }

    @Override
    public ImmutableByte2ShortSortedMap subMap(byte fromKey, byte toKey) {
        if (compare(fromKey, toKey) > 0) {
            throw new IllegalArgumentException();
        }

        int from = 0;
        int to = keySet.elements.length;

        while (compare(keySet.elements[from], fromKey) < 0) {
            from++;

            if (from >= keySet.elements.length) {
                return ImmutableByte2ShortSortedMap.of(comparator());
            }
        }

        while (compare(keySet.elements[--to], toKey) > 0) {
            if (to <= 0) {
                return ImmutableByte2ShortSortedMap.of(comparator());
            }
        }

        if (from > ++to) {
            throw new IllegalArgumentException();
        }

        if (from == to) {
            return ImmutableByte2ShortSortedMap.of(comparator());
        }

        if (from == 0 && to == keySet.elements.length) {
            return this;
        }

        int h = 0;
        byte[] elements = Arrays.copyOfRange(keySet.elements, from, to);

        for (byte b : elements) {
            int i = Byte.toUnsignedInt(b);
            short v = table[i];

            h += Byte.hashCode(b) ^ Short.hashCode(v);
        }

        return rangeCopy(h, table, new ImmutableByteTableSortedSet(elements, comparator()));
    }

    @Override
    public ImmutableByte2ShortSortedMap tailMap(byte fromKey) {
        int from = 0;

        while (compare(keySet.elements[from], fromKey) < 0) {
            from++;

            if (from >= keySet.elements.length) {
                return ImmutableByte2ShortSortedMap.of(comparator());
            }
        }

        int h = 0;
        byte[] elements = Arrays.copyOfRange(keySet.elements, from, keySet.elements.length);

        for (byte b : elements) {
            int i = Byte.toUnsignedInt(b);
            short v = table[i];

            h += Byte.hashCode(b) ^ Short.hashCode(v);
        }

        return rangeCopy(h, table, new ImmutableByteTableSortedSet(elements, comparator()));
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

    protected int compare(byte a, byte b) {
        return comparator().compare(a, b);
    }

    protected ImmutableByte2ShortTableSortedMap rangeCopy(int hashCode, short[] table,
                                                          ImmutableByteTableSortedSet keySet) {
        return new ImmutableByte2ShortTableSortedMap(hashCode, table, keySet);
    }
}
