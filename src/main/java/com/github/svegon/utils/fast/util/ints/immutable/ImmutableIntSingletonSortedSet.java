package com.github.svegon.utils.fast.util.ints.immutable;

import com.github.svegon.utils.collections.SetType;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntIterators;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.jcip.annotations.Immutable;

@Immutable
public class ImmutableIntSingletonSortedSet extends ImmutableIntSortedSet {
    private final IntComparator comparator;
    final int element;

    ImmutableIntSingletonSortedSet(final IntComparator comparator, final int element) {
        super(Integer.hashCode(element));
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
    public IntBidirectionalIterator iterator(int fromElement) {
        IntBidirectionalIterator i = IntIterators.singleton(element);

        if (compare(element, fromElement) < 0) {
            i.nextInt();
        }

        return i;
    }

    @Override
    public IntBidirectionalIterator iterator() {
        return IntIterators.singleton(element);
    }

    @Override
    public IntSortedSet subSet(int fromElement, int toElement) {
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
    public IntSortedSet tailSet(int fromElement) {
        return compare(element, fromElement) < 0 ? of(comparator()) : this;
    }

    @Override
    public IntComparator comparator() {
        return comparator;
    }

    @Override
    public int firstInt() {
        return element;
    }

    @Override
    public int lastInt() {
        return element;
    }

    protected int compare(int a, int b) {
        return comparator().compare(a, b);
    }

    @Override
    public SetType getType() {
        return SetType.TABLE;
    }

    @Override
    public boolean contains(int key) {
        return element == key;
    }
}
