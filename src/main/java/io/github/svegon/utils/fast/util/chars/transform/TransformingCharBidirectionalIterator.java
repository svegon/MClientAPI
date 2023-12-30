package io.github.svegon.utils.fast.util.chars.transform;

import it.unimi.dsi.fastutil.BidirectionalIterator;
import it.unimi.dsi.fastutil.chars.CharBidirectionalIterator;

public abstract class TransformingCharBidirectionalIterator<E, I extends BidirectionalIterator<E>>
        extends TransformingCharIterator<E, I> implements CharBidirectionalIterator {
    protected TransformingCharBidirectionalIterator(I itr) {
        super(itr);
    }

    @Override
    public final boolean hasPrevious() {
        return itr.hasPrevious();
    }
}
