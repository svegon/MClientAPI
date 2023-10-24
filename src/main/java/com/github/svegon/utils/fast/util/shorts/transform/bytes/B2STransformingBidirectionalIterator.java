package com.github.svegon.utils.fast.util.shorts.transform.bytes;

import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.bytes.ByteBidirectionalIterator;

public class B2STransformingBidirectionalIterator
        extends TransformingShortBidirectionalIterator<Byte, ByteBidirectionalIterator> {
    private final Byte2ShortFunction transformer;

    public B2STransformingBidirectionalIterator(ByteBidirectionalIterator itr, Byte2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short previousShort() {
        return transformer.apply(itr.nextByte());
    }

    @Override
    public short nextShort() {
        return transformer.apply(itr.nextByte());
    }

    public Byte2ShortFunction getTransformer() {
        return transformer;
    }
}
