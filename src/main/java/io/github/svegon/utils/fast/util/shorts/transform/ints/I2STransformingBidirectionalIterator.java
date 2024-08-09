package io.github.svegon.utils.fast.util.shorts.transform.ints;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;

public class I2STransformingBidirectionalIterator
        extends TransformingShortBidirectionalIterator<Integer, IntBidirectionalIterator> {
    private final Int2ShortFunction transformer;

    public I2STransformingBidirectionalIterator(IntBidirectionalIterator itr, Int2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short previousShort() {
        return transformer.apply(itr.nextInt());
    }

    @Override
    public short nextShort() {
        return transformer.apply(itr.nextInt());
    }

    public Int2ShortFunction getTransformer() {
        return transformer;
    }
}
