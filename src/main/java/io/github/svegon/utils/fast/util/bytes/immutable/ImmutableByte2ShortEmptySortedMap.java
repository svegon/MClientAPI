package io.github.svegon.utils.fast.util.bytes.immutable;

import io.github.svegon.utils.collections.MultisetUtil;
import io.github.svegon.utils.fast.util.shorts.ShortMultiset;
import it.unimi.dsi.fastutil.bytes.ByteComparator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

@Immutable
public class ImmutableByte2ShortEmptySortedMap extends ImmutableByte2ShortSortedMap {
    public static final ImmutableByte2ShortEmptySortedMap DEFAULT_SORTED =
            new ImmutableByte2ShortEmptySortedMap(Byte::compare) {
                public ByteComparator comparator() {
                    return null;
                }

                @Override
                public ImmutableByteSortedSet keySet() {
                    return EmptyImmutableByteSortedSet.DEFAULT;
                }
            };

    private final @NotNull ByteComparator comparator;

    ImmutableByte2ShortEmptySortedMap(final @NotNull ByteComparator comparator) {
        super(0);
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ShortMultiset values() {
        return MultisetUtil.emptyShortMultiset();
    }

    @Override
    public short get(byte key) {
        return defaultReturnValue();
    }

    @Override
    public boolean containsKey(byte key) {
        return false;
    }

    @Override
    public boolean containsValue(short value) {
        return false;
    }

    @Override
    public short getOrDefault(byte key, short defaultValue) {
        return defaultValue;
    }

    @Override
    public ObjectSortedSet<Entry> byte2ShortEntrySet() {
        return (ObjectSortedSet<Entry>) ObjectSortedSets.<Entry>emptySet();
    }

    @Override
    public ImmutableByteSortedSet keySet() {
        return ImmutableByteSortedSet.of(comparator());
    }

    @Override
    public ImmutableByte2ShortSortedMap subMap(byte fromKey, byte toKey) {
        if (comparator.compare(fromKey, toKey) > 0) {
            throw new IllegalArgumentException("fromKey " + fromKey + " is larger than toKey " + toKey);
        }

        return this;
    }

    @Override
    public ImmutableByte2ShortSortedMap headMap(byte toKey) {
        return this;
    }

    @Override
    public ImmutableByte2ShortSortedMap tailMap(byte fromKey) {
        return this;
    }

    @Override
    public byte firstByteKey() {
        throw new NoSuchElementException();
    }

    @Override
    public byte lastByteKey() {
        throw new NoSuchElementException();
    }

    @Override
    public ByteComparator comparator() {
        return comparator;
    }
}
