package com.github.svegon.utils.fast.util.ints.transform.shorts;

import com.github.svegon.utils.fast.util.ints.transform.TransformingIntBidirectionalIterator;
import com.github.svegon.utils.fast.util.ints.transform.TransformingIntListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;

public class S2ITransformingBidirectionalIterator
        extends TransformingIntBidirectionalIterator<Short, ShortListIterator> {
    private final Short2IntFunction transformer;

    public S2ITransformingBidirectionalIterator(ShortListIterator itr, Short2IntFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int previousInt() {
        return transformer.get(itr.previousShort());
    }

    @Override
    public int nextInt() {
        return transformer.get(itr.nextShort());
    }


    public Short2IntFunction getTransformer() {
        return transformer;
    }
}
