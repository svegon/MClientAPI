package com.github.svegon.utils.fast.util.chars.transform.shorts;

import com.github.svegon.utils.fast.util.chars.transform.TransformingCharBidirectionalIterator;
import com.github.svegon.utils.fast.util.chars.transform.TransformingCharListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.ShortBidirectionalIterator;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;

public class S2CTransformingBidirectionalIterator
        extends TransformingCharBidirectionalIterator<Short, ShortBidirectionalIterator> {
    private final Short2CharFunction transformer;

    public S2CTransformingBidirectionalIterator(ShortBidirectionalIterator itr, Short2CharFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.get(itr.nextShort());
    }

    @Override
    public char previousChar() {
        return transformer.get(itr.previousShort());
    }
}
