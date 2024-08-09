package io.github.svegon.utils.fast.util.shorts.transform.ints;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.ints.IntListIterator;

public class I2STransformingListIterator extends TransformingShortListIterator<Integer, IntListIterator> {
    private final Int2ShortFunction transformer;

    public I2STransformingListIterator(IntListIterator itr, Int2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.get(itr.nextInt());
    }

    @Override
    public short previousShort() {
        return transformer.get(itr.previousInt());
    }
}
