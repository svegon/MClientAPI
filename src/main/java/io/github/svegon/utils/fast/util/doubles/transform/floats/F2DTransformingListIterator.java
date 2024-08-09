package io.github.svegon.utils.fast.util.doubles.transform.floats;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.FloatListIterator;

public class F2DTransformingListIterator extends TransformingDoubleListIterator<Float, FloatListIterator> {
    private final Float2DoubleFunction transformer;

    public F2DTransformingListIterator(FloatListIterator itr, Float2DoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.get(itr.nextFloat());
    }

    @Override
    public double previousDouble() {
        return transformer.get(itr.previousFloat());
    }
}
