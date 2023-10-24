package com.github.svegon.utils.fast.util.chars.transform.booleans;

import com.github.svegon.utils.fast.util.chars.transform.TransformingCharBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2CharFunction;
import it.unimi.dsi.fastutil.booleans.BooleanBidirectionalIterator;

public class Z2CTransformingBidirectionalIterator
        extends TransformingCharBidirectionalIterator<Boolean, BooleanBidirectionalIterator> {
    private final Boolean2CharFunction transformer;

    public Z2CTransformingBidirectionalIterator(BooleanBidirectionalIterator itr, Boolean2CharFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char previousChar() {
        return transformer.apply(itr.nextBoolean());
    }

    @Override
    public char nextChar() {
        return transformer.apply(itr.nextBoolean());
    }

    public Boolean2CharFunction getTransformer() {
        return transformer;
    }
}
