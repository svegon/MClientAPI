package com.github.svegon.utils.fast.util.shorts.transform;

import it.unimi.dsi.fastutil.shorts.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.SortedSet;

public abstract class TransformingShortSortedSet<E, S extends SortedSet<E>> extends TransformingShortSet<E, S>
        implements ShortSortedSet {
    protected TransformingShortSortedSet(S set) {
        super(set);
    }

    @Override
    public abstract ShortBidirectionalIterator iterator(short fromElement);

    @Override
    public abstract ShortBidirectionalIterator iterator();

    @Override
    public ShortSpliterator spliterator() {
        return ShortSortedSet.super.spliterator();
    }

    @Override
    public abstract boolean contains(short o);

    @Override
    public abstract boolean removeIf(ShortPredicate filter);

    @Nullable
    @Override
    public abstract ShortComparator comparator();

    @NotNull
    @Override
    public abstract ShortSortedSet subSet(short fromElement, short toElement);

    @NotNull
    @Override
    public abstract ShortSortedSet headSet(short toElement);

    @NotNull
    @Override
    public abstract ShortSortedSet tailSet(short fromElement);

    @Override
    public final short firstShort() {
        return iterator().nextShort();
    }

    @Override
    public abstract short lastShort();
}
