package com.github.svegon.utils.fast.util.ints.immutable;

import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.NoSuchElementException;

public abstract class AbstractImmutableIntSortedSet extends ImmutableIntSortedSet {
    final int[] elements;
    final IntComparator comparator;

    AbstractImmutableIntSortedSet(int hashCode, int[] elements, @Nullable IntComparator comparator) {
        super(hashCode);
        this.elements = elements;
        this.comparator = comparator;
    }

    @Override
    public final ImmutableIntList toList() {
        return new RegularImmutableIntList(elements);
    }

    @Override
    public final IntListIterator iterator(int fromElement) {
        int i = 0;

        while (i < elements.length && compare(elements[i], fromElement) < 0) {
            i++;
        }

        return new Itr(elements, i);
    }

    @Override
    public final int size() {
        return elements.length;
    }

    @Override
    public final @NotNull IntListIterator iterator() {
        return new Itr(elements, 0);
    }

    @Override
    public IntSortedSet subSet(int fromElement, int toElement) {
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

        if (to <= from) {
            return of(comparator());
        }

        if (from == 0 && to == elements.length) {
            return this;
        }

        return of(Arrays.copyOfRange(elements, from, to), comparator(), this);
    }

    @Override
    public IntSortedSet tailSet(int fromElement) {
        int from = 0;

        while (compare(elements[from], fromElement) < 0) {
            from++;

            if (from >= elements.length) {
                return of(comparator());
            }
        }

        return of(Arrays.copyOfRange(elements, from, elements.length), comparator(), this);
    }

    @Override
    public final IntComparator comparator() {
        return comparator;
    }

    @Override
    public final int firstInt() {
        return elements[0];
    }

    @Override
    public final int lastInt() {
        return elements[elements.length - 1];
    }

    protected int compare(int a, int b) {
        return comparator().compare(a, b);
    }

    static final class Itr implements IntListIterator {
        private final int[] elements;
        private int index;

        Itr(int[] elements, int offset) {
            this.elements = elements;
            this.index = offset;
        }

        @Override
        public int previousInt() {
            try {
                return elements[--index];
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
        public int nextInt() {
            try {
                return elements[index++];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchElementException(e);
            }
        }

        @Override
        public boolean hasNext() {
            return index < elements.length;
        }
    }
}
