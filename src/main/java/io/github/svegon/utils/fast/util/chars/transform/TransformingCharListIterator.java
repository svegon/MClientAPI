package io.github.svegon.utils.fast.util.chars.transform;

import it.unimi.dsi.fastutil.chars.CharListIterator;

import java.util.ListIterator;

public abstract class TransformingCharListIterator<E, I extends ListIterator<E>> extends TransformingCharIterator<E, I>
        implements CharListIterator {
    protected TransformingCharListIterator(I itr) {
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
