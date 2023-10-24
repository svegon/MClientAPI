package com.github.svegon.utils.fast.util.bytes.immutable;

import com.github.svegon.utils.collections.ArrayUtil;
import it.unimi.dsi.fastutil.bytes.ByteBidirectionalIterator;
import it.unimi.dsi.fastutil.bytes.ByteComparator;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.bytes.ByteSortedSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Immutable
public class ImmutableByteTableSortedSet extends ImmutableByteSortedSet {
    final byte[] elements;
    final boolean[] table;
    private final @Nullable ByteComparator comparator;

    ImmutableByteTableSortedSet(int hashCode, byte[] elements, boolean[] table, @Nullable ByteComparator comparator) {
        super(hashCode);
        this.elements = elements;
        this.table = table;
        this.comparator = comparator;
    }

    ImmutableByteTableSortedSet(byte[] elements, boolean[] table, @Nullable ByteComparator comparator) {
        this(hash(elements, table), elements, table, comparator);
    }

    ImmutableByteTableSortedSet(byte[] elements, @Nullable ByteComparator comparator) {
        this(elements, new boolean[256], comparator);
    }

    @Override
    public ImmutableByteList toList() {
        return new RegularImmutableByteList(elements);
    }

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public boolean contains(byte key) {
        return table[Byte.toUnsignedInt(key)];
    }

    @Override
    public ByteBidirectionalIterator iterator(byte fromElement) {
        return new Itr(elements, Math.max(ArrayUtil.asList(elements).indexOf(fromElement), 0));
    }

    @Override
    public @NotNull ByteBidirectionalIterator iterator() {
        return new Itr(elements, 0);
    }

    @Override
    public ByteSortedSet subSet(byte fromElement, byte toElement) {
        if (compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }

        int from = 0;
        int to = elements.length;

        while (compare(elements[from], fromElement) < 0) {
            from++;

            if (from >= elements.length) {
                return of(comparator());
            }
        }

        while (compare(elements[--to], toElement) > 0) {
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

        if (from == 0 && to == elements.length) {
            return this;
        }

        return of(Arrays.copyOfRange(elements, from, to), comparator());
    }

    @Override
    public ByteSortedSet tailSet(byte fromElement) {
        int from = 0;

        while (compare(elements[from], fromElement) < 0) {
            from++;

            if (from >= elements.length) {
                return of(comparator());
            }
        }

        return of(Arrays.copyOfRange(elements, from, elements.length), comparator());
    }

    @Override
    public ByteComparator comparator() {
        return comparator;
    }

    @Override
    public byte firstByte() {
        return elements[0];
    }

    @Override
    public byte lastByte() {
        return elements[elements.length - 1];
    }

    private static class Itr implements ByteListIterator {
        private final byte[] elements;
        private final int offset;
        private int index;

        private Itr(byte[] elements, int offset) {
            this.elements = elements;
            this.offset = offset;
        }

        @Override
        public byte previousByte() {
            try {
                return elements[offset + --index];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchElementException(e);
            }
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public byte nextByte() {
            try {
                return elements[offset + index++];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchElementException(e);
            }
        }

        @Override
        public boolean hasNext() {
            return index + offset < elements.length;
        }
    }

    protected int compare(byte a, byte b) {
        return comparator().compare(a, b);
    }

    protected ImmutableByteTableSortedSet of(byte[] elements, ByteComparator comparator) {
        return new ImmutableByteTableSortedSet(elements, comparator);
    }

    private static int hash(byte[] elements, boolean[] table) {
        int h = 0;

        for (byte b : elements) {
            h += Byte.hashCode(b);
            table[Byte.toUnsignedInt(b)] = true;
        }

        return h;
    }
}
