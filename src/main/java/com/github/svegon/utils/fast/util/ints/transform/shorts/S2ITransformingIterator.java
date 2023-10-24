package com.github.svegon.utils.fast.util.ints.transform.shorts;


import com.github.svegon.utils.fast.util.ints.transform.TransformingIntIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.ShortIterator;

public class S2ITransformingIterator extends TransformingIntIterator<Short, ShortIterator> {
    private final Short2IntFunction transformer;

    public S2ITransformingIterator(ShortIterator itr, Short2IntFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int nextInt() {
        return transformer.apply(itr.nextShort());
    }
}
