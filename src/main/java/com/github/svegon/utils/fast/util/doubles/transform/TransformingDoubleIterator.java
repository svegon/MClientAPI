package com.github.svegon.utils.fast.util.doubles.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;

import java.util.Iterator;

public abstract class TransformingDoubleIterator<E, I extends Iterator<E>> implements DoubleIterator {
    protected final I itr;

    protected TransformingDoubleIterator(I itr) {
        this.itr = Preconditions.checkNotNull(itr);
    }

    @Override
    public final boolean hasNext() {
        return itr.hasNext();
    }

    @Override
    public final void remove() {
        itr.remove();
    }
}
