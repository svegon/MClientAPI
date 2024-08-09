package io.github.svegon.utils.fast.util.floats.transform.shorts;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.ShortIterator;

public class S2FTransformingIterator extends TransformingFloatIterator<Short, ShortIterator> {
    private final Short2FloatFunction transformer;

    protected S2FTransformingIterator(ShortIterator itr, Short2FloatFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.get(itr.nextShort());
    }

    @Override
    public int skip(int n) {
        return itr.skip(n);
    }
}
