package com.github.svegon.utils.fast.util.bytes.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.ByteIterator;

import java.util.Iterator;

public abstract class TransformingByteIterator<E, I extends Iterator<E>> implements ByteIterator {
    protected final I itr;

    protected TransformingByteIterator(I itr) {
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
