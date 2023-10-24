package com.github.svegon.utils.fast.util.doubles.transform.floats;

import com.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.FloatIterator;

public class F2DTransformingIterator extends TransformingDoubleIterator<Float, FloatIterator> {
    private final Float2DoubleFunction transformer;

    public F2DTransformingIterator(FloatIterator itr, Float2DoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.get(itr.nextFloat());
    }
}
