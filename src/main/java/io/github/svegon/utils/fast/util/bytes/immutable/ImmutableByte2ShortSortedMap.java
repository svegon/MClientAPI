package io.github.svegon.utils.fast.util.bytes.immutable;

import io.github.svegon.utils.fast.util.bytes.shorts.Byte2ShortTableMap;
import it.unimi.dsi.fastutil.bytes.Byte2ShortMap;
import it.unimi.dsi.fastutil.bytes.Byte2ShortSortedMap;
import it.unimi.dsi.fastutil.bytes.ByteComparator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Immutable
public abstract class ImmutableByte2ShortSortedMap extends ImmmutableByte2ShortMap implements Byte2ShortSortedMap {
    ImmutableByte2ShortSortedMap(int hashCode) {
        super(hashCode);
    }

    @Override
    public final ImmutableByte2ShortSortedMap clone() {
        return this;
    }

    @Override
    public abstract ObjectSortedSet<Entry> byte2ShortEntrySet();

    @Override
    public abstract ImmutableByteSortedSet keySet();

    @Override
    public abstract Byte2ShortSortedMap subMap(byte fromKey, byte toKey);

    @Override
    public Byte2ShortSortedMap headMap(byte toKey) {
        return subMap(firstByteKey(), toKey);
    }

    @Override
    public abstract Byte2ShortSortedMap tailMap(byte fromKey);

    @Override
    public abstract byte firstByteKey();

    @Override
    public abstract byte lastByteKey();

    @Override
    public abstract ByteComparator comparator();

    public static ImmutableByte2ShortSortedMap copyOf(final @Nullable ByteComparator comparator,
                                                      final @NotNull Byte2ShortMap map) {
        return of(new Byte2ShortTableMap(map), comparator);
    }

    public static ImmutableByte2ShortSortedMap copyOf(final @NotNull Byte2ShortSortedMap map) {
        if (map instanceof ImmutableByte2ShortSortedMap) {
            return (ImmutableByte2ShortSortedMap) map;
        }

        return copyOf(map.comparator(), map);
    }

    public static ImmutableByte2ShortSortedMap copyOf(final @Nullable ByteComparator comparator,
                                                      final @NotNull Iterable<Entry> entries) {
        Byte2ShortTableMap values = new Byte2ShortTableMap();

        for (Entry entry : entries) {
            values.put(entry.getByteKey(), entry.getShortValue());
        }

        return of(values, comparator);
    }

    public static ImmutableByte2ShortEmptySortedMap of(final @Nullable ByteComparator comparator) {
        return comparator == null ? ImmutableByte2ShortEmptySortedMap.DEFAULT_SORTED
                : new ImmutableByte2ShortEmptySortedMap(comparator);
    }

    public static ImmutableByte2ShortSingletonSortedMap of(final @Nullable ByteComparator comparator, final byte key,
                                                          final short value) {
        return new ImmutableByte2ShortSingletonSortedMap(ImmutableByteSortedSet.of(comparator, key), value);
    }

    public static ImmutableByte2ShortSortedMap of(final @Nullable ByteComparator comparator, final byte k0,
                                                  final short v0, final byte k1, final short v1) {
        Byte2ShortTableMap map = new Byte2ShortTableMap();

        map.put(k0, v0);
        map.put(k1, v1);

        return of(map, comparator);
    }

    public static ImmutableByte2ShortSortedMap of(final @Nullable ByteComparator comparator, final byte k0,
                                                  final short v0, final byte k1, final short v1,
                                                  final byte k2, final short v2) {
        Byte2ShortTableMap map = new Byte2ShortTableMap();

        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);

        return of(map, comparator);
    }

    public static ImmutableByte2ShortSortedMap of(final @Nullable ByteComparator comparator, final byte k0,
                                                  final short v0, final byte k1, final short v1,
                                                  final byte k2, final short v2, final byte k3, final short v3) {
        Byte2ShortTableMap map = new Byte2ShortTableMap();

        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);

        return of(map, comparator);
    }

    public static ImmutableByte2ShortSortedMap of(final @Nullable ByteComparator comparator, final byte k0,
                                                  final short v0, final byte k1, final short v1,
                                                  final byte k2, final short v2, final byte k3, final short v3,
                                                  final byte k4, final short v4) {
        Byte2ShortTableMap map = new Byte2ShortTableMap();

        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);

        return of(map, comparator);
    }

    public static ImmutableByte2ShortSortedMap of(final @Nullable ByteComparator comparator, final byte k0,
                                                  final short v0, final byte k1, final short v1,
                                                  final byte k2, final short v2, final byte k3, final short v3,
                                                  final byte k4, final short v4, final byte k5, final short v5) {
        Byte2ShortTableMap map = new Byte2ShortTableMap();

        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);

        return of(map, comparator);
    }

    static ImmutableByte2ShortSortedMap of(final Byte2ShortTableMap values,
                                           final @Nullable ByteComparator comparator) {
        if (values.isEmpty()) {
            return of(comparator);
        }

        if (values.size() == 1) {
            return of(comparator, values.keySet().iterator().nextByte(), values.values().iterator().nextShort());
        }

        return comparator == null ? new ImmutableByte2ShortTableDefaultSortedMap(values)
                : new ImmutableByte2ShortTableSortedMap(comparator, values);
    }
}
