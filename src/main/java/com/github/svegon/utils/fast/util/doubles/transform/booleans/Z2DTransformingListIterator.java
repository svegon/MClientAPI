package com.github.svegon.utils.fast.util.doubles.transform.booleans;

import com.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction;
import it.unimi.dsi.fastutil.booleans.BooleanListIterator;

public class Z2DTransformingListIterator extends TransformingDoubleListIterator<Boolean, BooleanListIterator> {
    private final Boolean2DoubleFunction transformer;

    public Z2DTransformingListIterator(BooleanListIterator itr, Boolean2DoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.get(itr.nextBoolean());
    }

    @Override
    public double previousDouble() {
        return transformer.get(itr.previousBoolean());
    }
}
