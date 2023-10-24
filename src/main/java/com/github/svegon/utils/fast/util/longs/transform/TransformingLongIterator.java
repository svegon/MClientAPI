package com.github.svegon.utils.fast.util.longs.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.LongIterator;

import java.util.Iterator;

public abstract class TransformingLongIterator<E, I extends Iterator<E>> implements LongIterator {
    protected final I itr;

    protected TransformingLongIterator(I itr) {
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
