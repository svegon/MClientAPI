package com.github.svegon.utils.fast.util.chars.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Iterator;

public abstract class TransformingCharIterator<E, I extends Iterator<E>> implements CharIterator {
    protected final I itr;

    protected TransformingCharIterator(I itr) {
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
