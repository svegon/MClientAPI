package io.github.svegon.utils.fast.util.bytes.immutable;

import it.unimi.dsi.fastutil.bytes.ByteBidirectionalIterator;
import it.unimi.dsi.fastutil.bytes.ByteComparator;
import it.unimi.dsi.fastutil.bytes.ByteIterators;
import it.unimi.dsi.fastutil.bytes.ByteSortedSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

@Immutable
public final class EmptyImmutableByteSortedSet extends ImmutableByteSortedSet {
    public static final EmptyImmutableByteSortedSet DEFAULT = new EmptyImmutableByteSortedSet(null);

    private final @Nullable ByteComparator comparator;

    public EmptyImmutableByteSortedSet(@Nullable ByteComparator comparator) {
        super(0);
        this.comparator = comparator;
    }

    @Override
    public ImmutableByteList toList() {
        return RegularImmutableByteList.EMPTY;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean contains(byte key) {
        return false;
    }

    @Override
    public ByteBidirectionalIterator iterator(byte fromElement) {
        return ByteIterators.EMPTY_ITERATOR;
    }

    @Override
    public ByteSortedSet subSet(byte fromElement, byte toElement) {
        return this;
    }

    @Override
    public ByteSortedSet tailSet(byte fromElement) {
        return this;
    }

    @Override
    public ByteComparator comparator() {
        return comparator;
    }

    @Override
    public byte firstByte() {
        throw new NoSuchElementException();
    }

    @Override
    public byte lastByte() {
        throw new NoSuchElementException();
    }
}
