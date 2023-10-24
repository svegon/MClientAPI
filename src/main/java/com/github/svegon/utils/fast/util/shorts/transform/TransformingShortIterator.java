package com.github.svegon.utils.fast.util.shorts.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;

import java.util.Iterator;

public abstract class TransformingShortIterator<E, I extends Iterator<E>> implements ShortIterator {
    protected final I itr;

    protected TransformingShortIterator(I itr) {
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
