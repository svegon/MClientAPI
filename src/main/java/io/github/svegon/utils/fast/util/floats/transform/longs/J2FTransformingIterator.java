package io.github.svegon.utils.fast.util.floats.transform.longs;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.LongIterator;

public class J2FTransformingIterator extends TransformingFloatIterator<Long, LongIterator> {
    private final Long2FloatFunction transformer;

    protected J2FTransformingIterator(LongIterator itr, Long2FloatFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.get(itr.nextLong());
    }

    @Override
    public int skip(int n) {
        return itr.skip(n);
    }
}
