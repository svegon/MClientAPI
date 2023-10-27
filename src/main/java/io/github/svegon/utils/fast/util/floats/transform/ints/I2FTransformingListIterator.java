package io.github.svegon.utils.fast.util.floats.transform.ints;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.IntListIterator;

public class I2FTransformingListIterator extends TransformingFloatListIterator<Integer, IntListIterator> {
    private final Int2FloatFunction transformer;

    public I2FTransformingListIterator(IntListIterator itr, Int2FloatFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.get(itr.nextInt());
    }

    @Override
    public float previousFloat() {
        return transformer.get(itr.previousInt());
    }
}
