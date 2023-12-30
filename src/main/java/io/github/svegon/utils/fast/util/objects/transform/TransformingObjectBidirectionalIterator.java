package io.github.svegon.utils.fast.util.objects.transform;

import it.unimi.dsi.fastutil.BidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public abstract class TransformingObjectBidirectionalIterator<T, I extends BidirectionalIterator<T>, E>
        extends TransformingObjectIterator<T, I, E> implements ObjectBidirectionalIterator<E> {
    protected TransformingObjectBidirectionalIterator(I itr) {
        super(itr);
    }

    @Override
    public abstract E previous();

    @Override
    public final boolean hasPrevious() {
        return itr.hasPrevious();
    }

    @Override
    public abstract E next();
}
