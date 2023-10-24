package com.github.svegon.utils.fast.util.ints.transform;

import it.unimi.dsi.fastutil.ints.IntListIterator;

import java.util.ListIterator;

public abstract class TransformingIntListIterator<E, I extends ListIterator<E>>
        extends TransformingIntIterator<E, I> implements IntListIterator {
    protected TransformingIntListIterator(I itr) {
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
