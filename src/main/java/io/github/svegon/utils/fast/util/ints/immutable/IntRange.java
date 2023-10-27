package io.github.svegon.utils.fast.util.ints.immutable;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.*;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;

import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.function.IntConsumer;

@Immutable
public class IntRange extends ImmutableIntList implements RandomAccess {
    final int from;
    private final int size;
    final int increment;

    IntRange(int from, int to, int increment) {
        super(hash(from, to));
        this.from = from;
        this.increment = increment;
        this.size = (to - from + 1) / increment;
    }

    @Override
    public IntListIterator listIterator(int index) {
        return new Itr(from, size, increment, index);
    }

    @Override
    public int getInt(int index) {
        Preconditions.checkElementIndex(index, size());
        return from + index;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int indexOf(int k) {
        if (k < from || k >= from + size) {
            return -1;
        }

        return k - from;
    }

    @Override
    public int lastIndexOf(int k) {
        return indexOf(k);
    }

    @Override
    public void forEach(IntConsumer action) {
        final int max = from + size;

        for (int i = from; i < max; i++) {
            action.accept(i);
        }
    }

    private static int hash(int from, int to) {
        int h = 1;

        while (from <= to) {
            h = 31 * h + from++;
        }

        return h;
    }

    @NotThreadSafe
    static final class Itr implements IntListIterator {
        final int offset;
        final int size;
        final int increment;
        int index;

        Itr(int offset, int size, int increment, int index) {
            this.offset = offset;
            this.size = size;
            this.increment = increment;
            this.index = index;
        }

        @Override
        public int previousInt() {
            if (index <= 0) {
                throw new NoSuchElementException();
            }

            return offset + increment * (--index);
        }

        @Override
        public int back(int n) {
            return index - Math.max(index - n, 0);
        }

        @Override
        public int skip(int n) {
            return Math.min(index + n, size) - index;
        }

        @Override
        public boolean hasPrevious() {
            return index >= 0;
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
        public int nextInt() {
            if (index >= size) {
                throw new NoSuchElementException();
            }

            return offset + increment * (index++);
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }
    }

    private static class RangeSpliterator extends IndexBasedImmutableSpliterator {
        private final IntComparator comparator;

        public RangeSpliterator(ImmutableIntList list, int index, int fence, IntComparator comparator) {
            super(list, index, fence);
            this.comparator = comparator;
        }

        public RangeSpliterator(IntRange list) {
            this(list, 0, list.size(), list.increment < 0 ? IntComparators.OPPOSITE_COMPARATOR : null);
        }

        @Override
        public IntSpliterator trySplit() {
            return new RangeSpliterator(list, start, index, comparator);
        }

        @Override
        public int characteristics() {
            return IntSpliterators.LIST_SPLITERATOR_CHARACTERISTICS | IMMUTABLE | DISTINCT
                    | SORTED;
        }

        @Override
        public IntComparator getComparator() {
            return comparator;
        }
    }
}
