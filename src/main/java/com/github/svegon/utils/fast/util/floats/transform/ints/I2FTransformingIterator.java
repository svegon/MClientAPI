package com.github.svegon.utils.fast.util.floats.transform.ints;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.IntIterator;

public class I2FTransformingIterator extends TransformingFloatIterator<Integer, IntIterator> {
    private final Int2FloatFunction transformer;

    protected I2FTransformingIterator(IntIterator itr, Int2FloatFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.get(itr.nextInt());
    }

    @Override
    public int skip(int n) {
        return itr.skip(n);
    }
}
