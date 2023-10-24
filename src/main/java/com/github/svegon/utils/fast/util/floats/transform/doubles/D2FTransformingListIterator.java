package com.github.svegon.utils.fast.util.floats.transform.doubles;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatListIterator;
import com.github.svegon.utils.interfaces.function.Object2FloatFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;

import java.util.ListIterator;

public class D2FTransformingListIterator extends TransformingFloatListIterator<Double, DoubleListIterator> {
    private final Double2FloatFunction transformer;

    public D2FTransformingListIterator(DoubleListIterator itr, Double2FloatFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.get(itr.nextDouble());
    }

    @Override
    public float previousFloat() {
        return transformer.get(itr.previousDouble());
    }
}
