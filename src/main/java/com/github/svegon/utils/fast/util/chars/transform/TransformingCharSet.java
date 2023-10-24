package com.github.svegon.utils.fast.util.chars.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.AbstractCharSet;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;

import java.util.Set;

public abstract class TransformingCharSet<E, S extends Set<E>> extends AbstractCharSet {
    protected final S set;

    public TransformingCharSet(S set) {
        this.set = Preconditions.checkNotNull(set);
    }

    @Override
    public final void clear() {
        set.clear();
    }

    @Override
    public abstract CharIterator iterator();

    @Override
    public abstract boolean contains(char i);

    @Override
    public abstract boolean removeIf(CharPredicate filter);

    @Override
    public final boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public final int size() {
        return set.size();
    }
}
