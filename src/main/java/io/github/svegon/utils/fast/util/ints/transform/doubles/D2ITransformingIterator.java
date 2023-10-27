package io.github.svegon.utils.fast.util.ints.transform.doubles;


import io.github.svegon.utils.fast.util.ints.transform.TransformingIntIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;

import java.util.function.DoubleToIntFunction;

public class D2ITransformingIterator extends TransformingIntIterator<Double, DoubleIterator> {
    private final DoubleToIntFunction transformer;

    public D2ITransformingIterator(DoubleIterator itr, DoubleToIntFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int nextInt() {
        return transformer.applyAsInt(itr.nextDouble());
    }
}
