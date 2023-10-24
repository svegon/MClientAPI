package com.github.svegon.utils.fast.util.chars.transform.ints;

import com.github.svegon.utils.fast.util.chars.transform.TransformingCharBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;

public class I2CTransformingBidirectionalIterator
        extends TransformingCharBidirectionalIterator<Integer, IntBidirectionalIterator> {
    private final Int2CharFunction transformer;

    public I2CTransformingBidirectionalIterator(IntBidirectionalIterator itr, Int2CharFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.get(itr.nextInt());
    }

    @Override
    public char previousChar() {
        return transformer.get(itr.previousInt());
    }
}
