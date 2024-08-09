package io.github.svegon.utils.fast.util.ints.transform.ints;


import io.github.svegon.utils.fast.util.ints.transform.TransformingIntIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntIterator;

import java.util.function.IntUnaryOperator;

public class I2ITransformingIterator extends TransformingIntIterator<Integer, IntIterator> {
    private final IntUnaryOperator transformer;

    public I2ITransformingIterator(IntIterator itr, IntUnaryOperator transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int nextInt() {
        return transformer.applyAsInt(itr.nextInt());
    }
}
