package io.github.svegon.utils.fast.util.booleans.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.BooleanListIterator;

import java.util.ListIterator;

public abstract class TransformingBooleanListIterator<E, I extends ListIterator<E>> implements BooleanListIterator {
    protected final I itr;

    protected TransformingBooleanListIterator(I itr) {
        this.itr = Preconditions.checkNotNull(itr);
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

    @Override
    public final boolean hasNext() {
        return itr.hasNext();
    }
}
