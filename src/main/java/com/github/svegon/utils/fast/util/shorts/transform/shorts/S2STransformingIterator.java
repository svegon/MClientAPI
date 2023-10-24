package com.github.svegon.utils.fast.util.shorts.transform.shorts;

import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortUnaryOperator;

public class S2STransformingIterator extends TransformingShortIterator<Short, ShortIterator> {
    private final ShortUnaryOperator transformer;

    public S2STransformingIterator(ShortIterator itr, ShortUnaryOperator transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.apply(itr.nextShort());
    }
}
