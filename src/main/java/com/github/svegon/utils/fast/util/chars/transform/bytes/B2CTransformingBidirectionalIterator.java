package com.github.svegon.utils.fast.util.chars.transform.bytes;

import com.github.svegon.utils.fast.util.chars.transform.TransformingCharBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.ByteBidirectionalIterator;

public class B2CTransformingBidirectionalIterator
        extends TransformingCharBidirectionalIterator<Byte, ByteBidirectionalIterator> {
    private final Byte2CharFunction transformer;

    public B2CTransformingBidirectionalIterator(ByteBidirectionalIterator itr, Byte2CharFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.get(itr.nextByte());
    }

    @Override
    public char previousChar() {
        return transformer.get(itr.previousByte());
    }
}
