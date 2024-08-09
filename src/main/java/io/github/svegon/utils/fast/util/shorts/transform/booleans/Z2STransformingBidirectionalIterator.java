package io.github.svegon.utils.fast.util.shorts.transform.booleans;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction;
import it.unimi.dsi.fastutil.booleans.BooleanBidirectionalIterator;

public class Z2STransformingBidirectionalIterator
        extends TransformingShortBidirectionalIterator<Boolean, BooleanBidirectionalIterator> {
    private final Boolean2ShortFunction transformer;

    public Z2STransformingBidirectionalIterator(BooleanBidirectionalIterator itr, Boolean2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short previousShort() {
        return transformer.apply(itr.nextBoolean());
    }

    @Override
    public short nextShort() {
        return transformer.apply(itr.nextBoolean());
    }

    public Boolean2ShortFunction getTransformer() {
        return transformer;
    }
}
