package io.github.svegon.utils.fast.util.doubles.transform;

import it.unimi.dsi.fastutil.doubles.DoubleListIterator;

import java.util.ListIterator;

public abstract class TransformingDoubleListIterator<E, I extends ListIterator<E>>
        extends TransformingDoubleIterator<E, I> implements DoubleListIterator {
    protected TransformingDoubleListIterator(I itr) {
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
