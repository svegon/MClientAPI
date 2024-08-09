package io.github.svegon.utils.fast.util.doubles.transform.longs;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.LongIterator;

import java.util.function.LongToDoubleFunction;

public class J2DTransformingIterator extends TransformingDoubleIterator<Long, LongIterator> {
    private final LongToDoubleFunction transformer;

    public J2DTransformingIterator(LongIterator itr, LongToDoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.applyAsDouble(itr.nextLong());
    }
}
