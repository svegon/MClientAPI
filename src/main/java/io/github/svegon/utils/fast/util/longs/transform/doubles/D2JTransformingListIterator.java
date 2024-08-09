package io.github.svegon.utils.fast.util.longs.transform.doubles;

import io.github.svegon.utils.fast.util.longs.transform.TransformingLongListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;

import java.util.function.DoubleToLongFunction;

public class D2JTransformingListIterator extends TransformingLongListIterator<Double, DoubleListIterator> {
    private final DoubleToLongFunction transformer;

    public D2JTransformingListIterator(DoubleListIterator itr, DoubleToLongFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public long nextLong() {
        return transformer.applyAsLong(itr.nextDouble());
    }

    @Override
    public long previousLong() {
        return transformer.applyAsLong(itr.previousDouble());
    }
}
