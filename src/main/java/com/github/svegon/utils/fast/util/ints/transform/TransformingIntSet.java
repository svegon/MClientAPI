package com.github.svegon.utils.fast.util.ints.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.AbstractIntSet;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Set;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public abstract class TransformingIntSet<E, S extends Set<E>> extends AbstractIntSet {
    protected final S set;

    public TransformingIntSet(S set) {
        this.set = Preconditions.checkNotNull(set);
    }

    @Override
    public final void clear() {
        set.clear();
    }

    @Override
    public abstract IntIterator iterator();

    @Override
    public abstract boolean contains(int i);

    @Override
    public abstract boolean removeIf(IntPredicate filter);

    @Override
    public final boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public final int size() {
        return set.size();
    }
}
