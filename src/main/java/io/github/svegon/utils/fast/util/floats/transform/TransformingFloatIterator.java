package io.github.svegon.utils.fast.util.floats.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;

import java.util.Iterator;

public abstract class TransformingFloatIterator<E, I extends Iterator<E>> implements FloatIterator {
    protected final I itr;

    protected TransformingFloatIterator(I itr) {
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
