package io.github.svegon.utils.fast.util.objects.transform.shorts;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.ShortBidirectionalIterator;

public class S2LTransformingBidirectionalIterator<E>
        extends TransformingObjectBidirectionalIterator<Short, ShortBidirectionalIterator, E> {
    private final Short2ObjectFunction<? extends E> transformer;

    public S2LTransformingBidirectionalIterator(ShortBidirectionalIterator itr,
                                                Short2ObjectFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.get(itr.nextShort());
    }

    @Override
    public E previous() {
        return transformer.get(itr.previousShort());
    }
}
