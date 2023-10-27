package io.github.svegon.utils.fast.util.bytes.immutable;

import io.github.svegon.utils.collections.CollectionUtil;
import io.github.svegon.utils.collections.MapUtil;
import io.github.svegon.utils.collections.SetUtil;
import io.github.svegon.utils.fast.util.bytes.ByteMultiset;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Immutable
public class ImmutableByte2ByteTableSortedMap extends ImmutableByte2ByteSortedMap {
    private final byte[] table;
    private final ImmutableByteTableSortedSet keySet;

    ImmutableByte2ByteTableSortedMap(int hashCode, byte[] table, ImmutableByteTableSortedSet keySet) {
        super(hashCode);
        this.table = table;
        this.keySet = keySet;
    }

    ImmutableByte2ByteTableSortedMap(int hashCode, ImmutableByteTableSortedSet keySet) {
        this(hashCode, new byte[256], keySet);
    }

    ImmutableByte2ByteTableSortedMap(@Nullable ByteComparator comparator, Byte2ByteOpenHashMap values) {
        this(values.hashCode(), new ImmutableByteTableSortedSet(values.keySet().toByteArray(), comparator));

        ByteArrays.unstableSort(keySet.elements, comparator);

        for (Entry e : values.byte2ByteEntrySet()) {
            table[Byte.toUnsignedInt(e.getByteKey())] = e.getByteValue();
        }
    }

    @Override
    public int size() {
        return keySet.size();
    }

    @Override
    public ByteMultiset values() {
        return CollectionUtil.mapToByte(keySet, b -> table[Byte.toUnsignedInt(b)]);
    }

    @Override
    public byte get(byte key) {
        return getOrDefault(key, defaultReturnValue());
    }

    @Override
    public boolean containsKey(byte key) {
        return keySet.contains(key);
    }

    @Override
    public boolean containsValue(byte value) {
        return values().contains(value);
    }

    @Override
    public byte getOrDefault(byte key, byte defaultValue) {
        return keySet.contains(key) ? defaultValue : table[Byte.toUnsignedInt(key)];
    }

    @Override
    public ObjectSortedSet<Entry> byte2ByteEntrySet() {
        return SetUtil.mapToObj(keySet, (b) -> MapUtil.immutableEntry(b, table[Byte.toUnsignedInt(b)]),
                Entry::getByteKey);
    }

    @Override
    public ImmutableByteSortedSet keySet() {
        return keySet;
    }

    @Override
    public Byte2ByteSortedMap subMap(byte fromKey, byte toKey) {
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
        byte[] elements = Arrays.copyOfRange(keySet.elements, from, to);

        for (byte b : elements) {
            int i = Byte.toUnsignedInt(b);
            byte v = table[i];

            h += Byte.hashCode(b) ^ Byte.hashCode(v);
        }

        return rangeCopy(h, table, new ImmutableByteTableSortedSet(elements, comparator()));
    }

    @Override
    public Byte2ByteSortedMap tailMap(byte fromKey) {
        int from = 0;

        while (compare(keySet.elements[from], fromKey) < 0) {
            from++;

            if (from >= keySet.elements.length) {
                return of(comparator());
            }
        }

        int h = 0;
        byte[] elements = Arrays.copyOfRange(keySet.elements, from, keySet.elements.length);

        for (byte b : elements) {
            int i = Byte.toUnsignedInt(b);
            byte v = table[i];

            h += Byte.hashCode(b) ^ Byte.hashCode(v);
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

    protected ImmutableByte2ByteTableSortedMap rangeCopy(int hashCode, byte[] table,
                                                         ImmutableByteTableSortedSet keySet) {
        return new ImmutableByte2ByteTableSortedMap(hashCode, table, keySet);
    }
}
