package io.github.svegon.utils.fast.util.shorts.transform;

import it.unimi.dsi.fastutil.shorts.ShortListIterator;

import java.util.ListIterator;

public abstract class TransformingShortListIterator<E, I extends ListIterator<E>>
        extends TransformingShortIterator<E, I> implements ShortListIterator {
    protected TransformingShortListIterator(I itr) {
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
