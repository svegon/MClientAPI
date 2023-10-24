package com.github.svegon.utils.fast.util.ints.transform;

import it.unimi.dsi.fastutil.BidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;

public abstract class TransformingIntBidirectionalIterator<E, I extends BidirectionalIterator<E>>
        extends TransformingIntIterator<E, I> implements IntBidirectionalIterator {
    protected TransformingIntBidirectionalIterator(I itr) {
        super(itr);
    }

    @Override
    public final boolean hasPrevious() {
        return itr.hasPrevious();
    }
}
