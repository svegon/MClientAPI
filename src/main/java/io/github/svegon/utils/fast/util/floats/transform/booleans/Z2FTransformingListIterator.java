package io.github.svegon.utils.fast.util.floats.transform.booleans;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction;
import it.unimi.dsi.fastutil.booleans.BooleanListIterator;

public class Z2FTransformingListIterator extends TransformingFloatListIterator<Boolean, BooleanListIterator> {
    private final Boolean2FloatFunction transformer;

    public Z2FTransformingListIterator(BooleanListIterator itr, Boolean2FloatFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.get(itr.nextBoolean());
    }

    @Override
    public float previousFloat() {
        return transformer.get(itr.previousBoolean());
    }
}
