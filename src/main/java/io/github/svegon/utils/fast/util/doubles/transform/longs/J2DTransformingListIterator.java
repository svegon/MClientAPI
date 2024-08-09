package io.github.svegon.utils.fast.util.doubles.transform.longs;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.LongListIterator;

import java.util.function.LongToDoubleFunction;

public class J2DTransformingListIterator extends TransformingDoubleListIterator<Long, LongListIterator> {
    private final LongToDoubleFunction transformer;

    public J2DTransformingListIterator(LongListIterator itr, LongToDoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.applyAsDouble(itr.nextLong());
    }

    @Override
    public double previousDouble() {
        return transformer.applyAsDouble(itr.previousLong());
    }
}
