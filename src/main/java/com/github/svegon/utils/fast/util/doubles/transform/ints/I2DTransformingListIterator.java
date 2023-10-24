package com.github.svegon.utils.fast.util.doubles.transform.ints;

import com.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntListIterator;

import java.util.function.IntToDoubleFunction;

public class I2DTransformingListIterator extends TransformingDoubleListIterator<Integer, IntListIterator> {
    private final IntToDoubleFunction transformer;

    public I2DTransformingListIterator(IntListIterator itr, IntToDoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.applyAsDouble(itr.nextInt());
    }

    @Override
    public double previousDouble() {
        return transformer.applyAsDouble(itr.previousInt());
    }
}
