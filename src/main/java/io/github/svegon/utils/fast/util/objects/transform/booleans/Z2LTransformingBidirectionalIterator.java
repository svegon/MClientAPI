package io.github.svegon.utils.fast.util.objects.transform.booleans;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import it.unimi.dsi.fastutil.booleans.BooleanBidirectionalIterator;

public class Z2LTransformingBidirectionalIterator<E>
        extends TransformingObjectBidirectionalIterator<Boolean, BooleanBidirectionalIterator, E> {
    private final Boolean2ObjectFunction<? extends E> transformer;

    public Z2LTransformingBidirectionalIterator(BooleanBidirectionalIterator itr,
                                                Boolean2ObjectFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.get(itr.nextBoolean());
    }

    @Override
    public E previous() {
        return transformer.get(itr.previousBoolean());
    }
}
