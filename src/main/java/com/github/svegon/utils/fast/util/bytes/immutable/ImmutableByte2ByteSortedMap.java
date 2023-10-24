package com.github.svegon.utils.fast.util.bytes.immutable;

import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@Immutable
public abstract class ImmutableByte2ByteSortedMap extends ImmmutableByte2ByteMap implements Byte2ByteSortedMap {
    ImmutableByte2ByteSortedMap(int hashCode) {
        super(hashCode);
    }

    @Override
    public final ImmutableByte2ByteSortedMap clone() {
        return this;
    }

    @Override
    public abstract ObjectSortedSet<Entry> byte2ByteEntrySet();

    @Override
    public abstract ImmutableByteSortedSet keySet();

    @Override
    public abstract Byte2ByteSortedMap subMap(byte fromKey, byte toKey);

    @Override
    public Byte2ByteSortedMap headMap(byte toKey) {
        return subMap(firstByteKey(), toKey);
    }

    @Override
    public abstract Byte2ByteSortedMap tailMap(byte fromKey);

    @Override
    public abstract byte firstByteKey();

    @Override
    public abstract byte lastByteKey();

    @Override
    public abstract ByteComparator comparator();

    public static ImmutableByte2ByteSortedMap copyOf(final @Nullable ByteComparator comparator,
                                                     final @NotNull Byte2ByteMap map) {
        return of(new Byte2ByteOpenHashMap(map), comparator);
    }

    public static ImmutableByte2ByteSortedMap copyOf(final @NotNull Byte2ByteSortedMap map) {
        if (map instanceof ImmutableByte2ByteSortedMap) {
            return (ImmutableByte2ByteSortedMap) map;
        }

        return copyOf(map.comparator(), map);
    }

    public static ImmutableByte2ByteSortedMap copyOf(final @Nullable ByteComparator comparator,
                                                     final @NotNull Iterable<Entry> entries) {
        Byte2ByteOpenHashMap values = entries instanceof Collection<?> c ? new Byte2ByteOpenHashMap(c.size())
                : new Byte2ByteOpenHashMap();

        for (Entry entry : entries) {
            values.put(entry.getByteKey(), entry.getByteValue());
        }

        return of(values, comparator);
    }

    public static ImmutableByte2ByteEmptySortedMap of(final @Nullable ByteComparator comparator) {
        return comparator == null ? ImmutableByte2ByteEmptySortedMap.DEFAULT_SORTED
                : new ImmutableByte2ByteEmptySortedMap(comparator);
    }

    public static ImmutableByte2ByteSingletonSortedMap of(final @Nullable ByteComparator comparator, final byte key,
                                                          final byte value) {
        return new ImmutableByte2ByteSingletonSortedMap(ImmutableByteSortedSet.of(comparator, key), value);
    }

    public static ImmutableByte2ByteSortedMap of(final @Nullable ByteComparator comparator,
                                                 final byte @NotNull ... keyValuePairs) {
        if ((keyValuePairs.length & 1) != 0) {
            throw new IllegalArgumentException("The number of keys and values isn't even!");
        }

        Byte2ByteOpenHashMap values = new Byte2ByteOpenHashMap(keyValuePairs.length / 2);

        for (int i = 0; i < keyValuePairs.length; i += 2) {
            values.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }

        return of(values, comparator);
    }

    static ImmutableByte2ByteSortedMap of(final Byte2ByteOpenHashMap values,
                                              final @Nullable ByteComparator comparator) {
        if (values.isEmpty()) {
            return of(comparator);
        }

        if (values.size() == 1) {
            return of(comparator, values.keySet().iterator().nextByte(), values.values().iterator().nextByte());
        }

        return comparator == null ? new ImmutableByte2ByteTableDefaultSortedMap(values)
                : new ImmutableByte2ByteTableSortedMap(comparator, values);
    }
}
