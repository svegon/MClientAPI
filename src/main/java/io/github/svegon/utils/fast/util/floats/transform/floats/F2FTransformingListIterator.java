package io.github.svegon.utils.fast.util.floats.transform.floats;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatListIterator;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;

public class F2FTransformingListIterator extends TransformingFloatListIterator<Float, FloatListIterator> {
    private final FloatUnaryOperator transformer;

    public F2FTransformingListIterator(FloatListIterator itr, FloatUnaryOperator transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.apply(itr.nextFloat());
    }

    @Override
    public float previousFloat() {
        return transformer.apply(itr.previousFloat());
    }
}
