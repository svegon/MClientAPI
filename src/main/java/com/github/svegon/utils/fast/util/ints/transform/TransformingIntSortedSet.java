package com.github.svegon.utils.fast.util.ints.transform;

import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.ints.IntSpliterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.SortedSet;
import java.util.function.IntPredicate;

public abstract class TransformingIntSortedSet<E, S extends SortedSet<E>> extends TransformingIntSet<E, S>
        implements IntSortedSet {
    protected TransformingIntSortedSet(S set) {
        super(set);
    }

    @Override
    public abstract IntBidirectionalIterator iterator(int fromElement);

    @Override
    public abstract IntBidirectionalIterator iterator();

    @Override
    public IntSpliterator spliterator() {
        return IntSortedSet.super.spliterator();
    }

    @Override
    public abstract boolean contains(int o);

    @Override
    public abstract boolean removeIf(IntPredicate filter);

    @Nullable
    @Override
    public abstract IntComparator comparator();

    @NotNull
    @Override
    public abstract IntSortedSet subSet(int fromElement, int toElement);

    @NotNull
    @Override
    public abstract IntSortedSet headSet(int toElement);

    @NotNull
    @Override
    public abstract IntSortedSet tailSet(int fromElement);

    @Override
    public final int firstInt() {
        return iterator().nextInt();
    }

    @Override
    public abstract int lastInt();
}
