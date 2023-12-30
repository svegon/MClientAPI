package io.github.svegon.utils.fast.util.floats.transform;

import it.unimi.dsi.fastutil.floats.FloatListIterator;

import java.util.ListIterator;

public abstract class TransformingFloatListIterator<E, I extends ListIterator<E>>
        extends TransformingFloatIterator<E, I> implements FloatListIterator {
    protected TransformingFloatListIterator(I itr) {
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
