package io.github.svegon.utils.fast.util.booleans.transform;

import io.github.svegon.utils.fast.util.booleans.transform.objects.TransformingBooleanIterator;
import it.unimi.dsi.fastutil.booleans.BooleanListIterator;

import java.util.ListIterator;

public abstract class TransformingBooleanListIterator<E, I extends ListIterator<E>> extends TransformingBooleanIterator<E, I>
        implements BooleanListIterator {
    protected TransformingBooleanListIterator(I itr) {
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
