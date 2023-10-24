package com.github.svegon.utils.fast.util.bytes.transform;

import it.unimi.dsi.fastutil.bytes.ByteListIterator;

import java.util.ListIterator;

public abstract class TransformingByteListIterator<E, I extends ListIterator<E>> extends TransformingByteIterator<E, I>
        implements ByteListIterator {
    protected TransformingByteListIterator(I itr) {
        super(itr);
    }

    @Override
    public final boolean hasPrevious() {
        return itr.hasPrevious();
    }

    @Override
    public final int nextIndex() {
        return itr.nextIndex();
    }

    @Override
    public final int previousIndex() {
        return itr.previousIndex();
    }
}
