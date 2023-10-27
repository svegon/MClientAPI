package io.github.svegon.utils.fast.util.shorts.transform.ints;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.ints.IntIterator;

public class I2STransformingIterator extends TransformingShortIterator<Integer, IntIterator> {
    private final Int2ShortFunction transformer;

    public I2STransformingIterator(IntIterator itr, Int2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.apply(itr.nextInt());
    }
}
