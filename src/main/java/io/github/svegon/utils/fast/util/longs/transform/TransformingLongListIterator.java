package io.github.svegon.utils.fast.util.longs.transform;

import it.unimi.dsi.fastutil.longs.LongListIterator;

import java.util.ListIterator;

public abstract class TransformingLongListIterator<E, I extends ListIterator<E>>
        extends TransformingLongIterator<E, I> implements LongListIterator {
    protected TransformingLongListIterator(I itr) {
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
