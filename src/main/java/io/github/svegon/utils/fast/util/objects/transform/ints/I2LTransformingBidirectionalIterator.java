package io.github.svegon.utils.fast.util.objects.transform.ints;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;

import java.util.function.IntFunction;

public class I2LTransformingBidirectionalIterator<E>
        extends TransformingObjectBidirectionalIterator<Integer, IntBidirectionalIterator, E> {
    private final IntFunction<? extends E> transformer;

    public I2LTransformingBidirectionalIterator(IntBidirectionalIterator itr,
                                                IntFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.apply(itr.nextInt());
    }

    @Override
    public E previous() {
        return transformer.apply(itr.previousInt());
    }
}
