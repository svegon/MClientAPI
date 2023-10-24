package com.github.svegon.utils.fast.util.bytes.transform;

import com.github.svegon.utils.fast.util.bytes.AbstractByteMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.BytePredicate;

import java.util.Collection;

public abstract class TransformingByteMultiset<T, C extends Collection<T>> extends AbstractByteMultiset {
    protected final C col;

    public TransformingByteMultiset(C col) {
        this.col = Preconditions.checkNotNull(col);
    }

    @Override
    public final void clear() {
        col.clear();
    }

    @Override
    public abstract ByteIterator iterator();

    @Override
    public abstract boolean contains(byte value);

    @Override
    public abstract boolean removeIf(BytePredicate filter);

    @Override
    public final boolean isEmpty() {
        return col.isEmpty();
    }

    @Override
    public final int size() {
        return col.size();
    }
}
