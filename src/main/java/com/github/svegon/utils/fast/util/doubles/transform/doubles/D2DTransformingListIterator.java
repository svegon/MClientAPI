package com.github.svegon.utils.fast.util.doubles.transform.doubles;

import com.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;

import java.util.ListIterator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.ToDoubleFunction;

public class D2DTransformingListIterator extends TransformingDoubleListIterator<Double, DoubleListIterator> {
    private final DoubleUnaryOperator transformer;

    public D2DTransformingListIterator(DoubleListIterator itr, DoubleUnaryOperator transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.applyAsDouble(itr.nextDouble());
    }

    @Override
    public double previousDouble() {
        return transformer.applyAsDouble(itr.previousDouble());
    }
}
