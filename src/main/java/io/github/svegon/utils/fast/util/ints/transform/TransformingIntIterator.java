package io.github.svegon.utils.fast.util.ints.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Iterator;

public abstract class TransformingIntIterator<E, I extends Iterator<E>> implements IntIterator {
    protected final I itr;

    protected TransformingIntIterator(I itr) {
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
