package com.github.svegon.utils.fast.util.ints.immutable;

import com.github.svegon.utils.ComparingUtil;
import com.github.svegon.utils.collections.SetType;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntIterators;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

@Immutable
public final class EmptyImmutableIntSortedSet extends ImmutableIntSortedSet {
    public static final EmptyImmutableIntSortedSet DEFAULT = new EmptyImmutableIntSortedSet(null);

    private final @Nullable IntComparator comparator;

    public EmptyImmutableIntSortedSet(@Nullable IntComparator comparator) {
        super(0);
        this.comparator = comparator;
    }

    @Override
    public SetType getType() {
        return SetType.TABLE;
    }

    @Override
    public ImmutableIntList toList() {
        return RegularImmutableIntList.EMPTY;
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
    public boolean contains(int key) {
        return false;
    }

    @Override
    public IntBidirectionalIterator iterator(int fromElement) {
        return IntIterators.EMPTY_ITERATOR;
    }

    @Override
    public IntBidirectionalIterator iterator() {
        return IntIterators.EMPTY_ITERATOR;
    }

    @Override
    public IntSortedSet subSet(int fromElement, int toElement) {
        if (ComparingUtil.compare(comparator(), fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }

        return this;
    }

    @Override
    public IntSortedSet tailSet(int fromElement) {
        return this;
    }

    @Override
    public IntComparator comparator() {
        return comparator;
    }

    @Override
    public int firstInt() {
        throw new NoSuchElementException();
    }

    @Override
    public int lastInt() {
        throw new NoSuchElementException();
    }
}
