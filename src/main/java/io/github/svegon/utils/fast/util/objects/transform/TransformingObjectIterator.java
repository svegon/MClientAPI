package io.github.svegon.utils.fast.util.objects.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Iterator;

public abstract class TransformingObjectIterator<T, I extends Iterator<T>, E> implements ObjectIterator<E> {
    protected final I itr;

    protected TransformingObjectIterator(I itr) {
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
