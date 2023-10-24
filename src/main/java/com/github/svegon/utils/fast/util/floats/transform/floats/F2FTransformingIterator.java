package com.github.svegon.utils.fast.util.floats.transform.floats;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;

public class F2FTransformingIterator extends TransformingFloatIterator<Float, FloatIterator> {
    private final FloatUnaryOperator transformer;

    protected F2FTransformingIterator(FloatIterator itr, FloatUnaryOperator transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.apply(itr.nextFloat());
    }

    @Override
    public int skip(int n) {
        return itr.skip(n);
    }
}
