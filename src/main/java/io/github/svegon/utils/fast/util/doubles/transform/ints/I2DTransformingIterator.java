package io.github.svegon.utils.fast.util.doubles.transform.ints;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntIterator;

import java.util.function.IntToDoubleFunction;

public class I2DTransformingIterator extends TransformingDoubleIterator<Integer, IntIterator> {
    private final IntToDoubleFunction transformer;

    public I2DTransformingIterator(IntIterator itr, IntToDoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.applyAsDouble(itr.nextInt());
    }
}
