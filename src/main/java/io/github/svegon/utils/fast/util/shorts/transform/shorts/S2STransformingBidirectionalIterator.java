package io.github.svegon.utils.fast.util.shorts.transform.shorts;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.ShortBidirectionalIterator;
import it.unimi.dsi.fastutil.shorts.ShortUnaryOperator;

public class S2STransformingBidirectionalIterator
        extends TransformingShortBidirectionalIterator<Short, ShortBidirectionalIterator> {
    private final ShortUnaryOperator transformer;

    public S2STransformingBidirectionalIterator(ShortBidirectionalIterator itr, ShortUnaryOperator transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short previousShort() {
        return transformer.apply(itr.nextShort());
    }

    @Override
    public short nextShort() {
        return transformer.apply(itr.nextShort());
    }

    public ShortUnaryOperator getTransformer() {
        return transformer;
    }
}
