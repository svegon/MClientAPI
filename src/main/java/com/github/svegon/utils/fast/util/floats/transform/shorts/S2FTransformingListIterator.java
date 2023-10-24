package com.github.svegon.utils.fast.util.floats.transform.shorts;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;

public class S2FTransformingListIterator extends TransformingFloatListIterator<Short, ShortListIterator> {
    private final Short2FloatFunction transformer;

    public S2FTransformingListIterator(ShortListIterator itr, Short2FloatFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.get(itr.nextShort());
    }

    @Override
    public float previousFloat() {
        return transformer.get(itr.previousShort());
    }
}
