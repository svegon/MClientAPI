package com.github.svegon.utils.fast.util.chars.transform.shorts;

import com.github.svegon.utils.fast.util.chars.transform.TransformingCharIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.ShortIterator;

public class S2CTransformingIterator extends TransformingCharIterator<Short, ShortIterator> {
    private final Short2CharFunction transformer;

    public S2CTransformingIterator(ShortIterator itr, Short2CharFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.apply(itr.nextShort());
    }
}
