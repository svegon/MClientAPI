package com.github.svegon.utils.fast.util.ints.transform;

import com.github.svegon.utils.fast.util.ints.AbstractIntMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntPredicate;

import java.util.Collection;

public abstract class TransformingIntMultiset<T, C extends Collection<T>> extends AbstractIntMultiset {
    protected final C col;

    public TransformingIntMultiset(C col) {
        this.col = Preconditions.checkNotNull(col);
    }

    @Override
    public final void clear() {
        col.clear();
    }

    @Override
    public abstract IntIterator iterator();

    @Override
    public abstract boolean contains(int value);

    @Override
    public abstract boolean removeIf(IntPredicate filter);

    @Override
    public final boolean isEmpty() {
        return col.isEmpty();
    }

    @Override
    public final int size() {
        return col.size();
    }
}
