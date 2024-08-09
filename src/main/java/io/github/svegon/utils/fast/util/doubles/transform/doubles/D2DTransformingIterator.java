package io.github.svegon.utils.fast.util.doubles.transform.doubles;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;

import java.util.function.DoubleUnaryOperator;

public class D2DTransformingIterator extends TransformingDoubleIterator<Double, DoubleIterator> {
    private final DoubleUnaryOperator transformer;

    public D2DTransformingIterator(DoubleIterator itr, DoubleUnaryOperator transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.applyAsDouble(itr.nextDouble());
    }
}
