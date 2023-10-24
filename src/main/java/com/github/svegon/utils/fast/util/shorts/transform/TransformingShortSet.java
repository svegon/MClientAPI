package com.github.svegon.utils.fast.util.shorts.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.AbstractShortSet;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;

import java.util.Set;

public abstract class TransformingShortSet<E, S extends Set<E>> extends AbstractShortSet {
    protected final S set;

    public TransformingShortSet(S set) {
        this.set = Preconditions.checkNotNull(set);
    }

    @Override
    public final void clear() {
        set.clear();
    }

    @Override
    public abstract ShortIterator iterator();

    @Override
    public abstract boolean contains(short i);

    @Override
    public abstract boolean removeIf(ShortPredicate filter);

    @Override
    public final boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public final int size() {
        return set.size();
    }
}
