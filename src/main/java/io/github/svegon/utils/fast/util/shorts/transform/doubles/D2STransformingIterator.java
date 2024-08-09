package io.github.svegon.utils.fast.util.shorts.transform.doubles;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;

public class D2STransformingIterator extends TransformingShortIterator<Double, DoubleIterator> {
    private final Double2ShortFunction transformer;

    public D2STransformingIterator(DoubleIterator itr, Double2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.get(itr.nextDouble());
    }
}
