package io.github.svegon.utils.fast.util.shorts.transform;

import it.unimi.dsi.fastutil.BidirectionalIterator;
import it.unimi.dsi.fastutil.shorts.ShortBidirectionalIterator;

public abstract class TransformingShortBidirectionalIterator<E, I extends BidirectionalIterator<E>>
        extends TransformingShortIterator<E, I> implements ShortBidirectionalIterator {
    protected TransformingShortBidirectionalIterator(I itr) {
        super(itr);
    }

    @Override
    public final boolean hasPrevious() {
        return itr.hasPrevious();
    }
}
