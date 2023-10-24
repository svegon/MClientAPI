package com.github.svegon.utils.fast.util.floats.transform.booleans;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;

public class Z2FTransformingIterator extends TransformingFloatIterator<Boolean, BooleanIterator> {
    private final Boolean2FloatFunction transformer;

    protected Z2FTransformingIterator(BooleanIterator itr, Boolean2FloatFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.get(itr.nextBoolean());
    }

    @Override
    public int skip(int n) {
        return itr.skip(n);
    }
}
