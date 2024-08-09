package io.github.svegon.utils.fast.util.bytes.immutable;

import io.github.svegon.utils.collections.MultisetUtil;
import io.github.svegon.utils.fast.util.bytes.ByteMultiset;
import it.unimi.dsi.fastutil.bytes.Byte2ByteSortedMap;
import it.unimi.dsi.fastutil.bytes.ByteComparator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

@Immutable
public class ImmutableByte2ByteEmptySortedMap extends ImmutableByte2ByteSortedMap {
    public static final ImmutableByte2ByteEmptySortedMap DEFAULT_SORTED =
            new ImmutableByte2ByteEmptySortedMap(Byte::compare) {
                public ByteComparator comparator() {
                    return null;
                }

                @Override
                public ImmutableByteSortedSet keySet() {
                    return EmptyImmutableByteSortedSet.DEFAULT;
                }
            };

    private final @NotNull ByteComparator comparator;

    ImmutableByte2ByteEmptySortedMap(final @NotNull ByteComparator comparator) {
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
    public ByteMultiset values() {
        return MultisetUtil.emptyByteMultiset();
    }

    @Override
    public byte get(byte key) {
        return defaultReturnValue();
    }

    @Override
    public boolean containsKey(byte key) {
        return false;
    }

    @Override
    public boolean containsValue(byte value) {
        return false;
    }

    @Override
    public byte getOrDefault(byte key, byte defaultValue) {
        return defaultValue;
    }

    @Override
    public ObjectSortedSet<Entry> byte2ByteEntrySet() {
        return (ObjectSortedSet<Entry>) ObjectSortedSets.<Entry>emptySet();
    }

    @Override
    public ImmutableByteSortedSet keySet() {
        return ImmutableByteSortedSet.of(comparator());
    }

    @Override
    public Byte2ByteSortedMap subMap(byte fromKey, byte toKey) {
        if (comparator.compare(fromKey, toKey) > 0) {
            throw new IllegalArgumentException("fromKey " + fromKey + " is larger than toKey " + toKey);
        }

        return this;
    }

    @Override
    public Byte2ByteSortedMap headMap(byte toKey) {
        return this;
    }

    @Override
    public Byte2ByteSortedMap tailMap(byte fromKey) {
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
