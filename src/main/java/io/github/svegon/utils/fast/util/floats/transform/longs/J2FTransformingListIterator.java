package io.github.svegon.utils.fast.util.floats.transform.longs;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.LongListIterator;

public class J2FTransformingListIterator extends TransformingFloatListIterator<Long, LongListIterator> {
    private final Long2FloatFunction transformer;

    public J2FTransformingListIterator(LongListIterator itr, Long2FloatFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.get(itr.nextLong());
    }

    @Override
    public float previousFloat() {
        return transformer.get(itr.previousLong());
    }
}
