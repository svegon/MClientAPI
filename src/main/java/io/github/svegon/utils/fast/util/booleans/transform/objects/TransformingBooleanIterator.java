package io.github.svegon.utils.fast.util.booleans.transform.objects;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;

import java.util.Iterator;

public abstract class TransformingBooleanIterator<E, I extends Iterator<E>> implements BooleanIterator {
    protected final I itr;

    protected TransformingBooleanIterator(I itr) {
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
