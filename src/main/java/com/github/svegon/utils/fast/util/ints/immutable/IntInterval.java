package com.github.svegon.utils.fast.util.ints.immutable;

import com.github.svegon.utils.collections.SetType;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.jcip.annotations.Immutable;

import java.util.NoSuchElementException;

@Immutable
public final class IntInterval extends ImmutableIntSortedSet {
    private final int from;
    private final int to;
    private final int size;

    IntInterval(int from, int to) {
        super(hash(from, to));
        this.from = from;
        this.to = to;
        this.size = to - from + 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(int key) {
        return from <= key && key <= to;
    }

    @Override
    public IntBidirectionalIterator iterator(int fromElement) {
        return new IntBidirectionalIterator() {
            int current = fromElement;

            @Override
            public int previousInt() {
                if (current <= from) {
                    throw new NoSuchElementException();
                }

                return --current;
            }

            @Override
            public boolean hasPrevious() {
                return current > from;
            }

            @Override
            public int nextInt() {
                if (current > to) {
                    throw new NoSuchElementException();
                }

                return current++;
            }

            @Override
            public boolean hasNext() {
                return current <= to;
            }
        };
    }

    @Override
    public IntBidirectionalIterator iterator() {
        return iterator(firstInt());
    }

    @Override
    public IntSortedSet subSet(int fromElement, int toElement) {
        return new IntInterval(fromElement, (int) (toElement - 1));
    }

    @Override
    public IntSortedSet tailSet(int fromElement) {
        return new IntInterval(fromElement, to);
    }

    @Override
    public IntComparator comparator() {
        return null;
    }

    @Override
    public int firstInt() {
        return from;
    }

    @Override
    public int lastInt() {
        return to;
    }

    private static int hash(int from, int to) {
        int h = 1;

        while (from <= to) {
            h = 31 * h + Integer.hashCode(from++);
        }

        return h;
    }

    @Override
    public SetType getType() {
        return SetType.TABLE;
    }
}
