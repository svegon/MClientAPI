package io.github.svegon.utils.fast.util.bytes.immutable;

import it.unimi.dsi.fastutil.bytes.ByteBidirectionalIterator;
import it.unimi.dsi.fastutil.bytes.ByteComparator;
import it.unimi.dsi.fastutil.bytes.ByteIterators;
import it.unimi.dsi.fastutil.bytes.ByteSortedSet;
import net.jcip.annotations.Immutable;

@Immutable
public class ImmutableByteSingletonSortedSet extends ImmutableByteSortedSet {
    private final ByteComparator comparator;
    final byte element;

    ImmutableByteSingletonSortedSet(final ByteComparator comparator, final byte element) {
        super(Byte.hashCode(element));
        this.comparator = comparator;
        this.element = element;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ByteBidirectionalIterator iterator(byte fromElement) {
        return compare(element, fromElement) >= 0 ? ByteIterators.singleton(element) : ByteIterators.EMPTY_ITERATOR;
    }

    @Override
    public ByteSortedSet subSet(byte fromElement, byte toElement) {
        int c = compare(fromElement, toElement);

        if (c > 0) {
            throw new IllegalArgumentException();
        }

        if (c == 0 || compare(element, fromElement) < 0) {
            return of(comparator());
        }

        return this;
    }

    @Override
    public ByteSortedSet tailSet(byte fromElement) {
        return compare(element, fromElement) < 0 ? of(comparator()) : this;
    }

    @Override
    public ByteComparator comparator() {
        return comparator;
    }

    @Override
    public byte firstByte() {
        return element;
    }

    @Override
    public byte lastByte() {
        return element;
    }

    protected int compare(byte a, byte b) {
        return comparator().compare(a, b);
    }
}
