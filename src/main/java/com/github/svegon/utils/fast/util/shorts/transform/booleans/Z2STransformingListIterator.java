package com.github.svegon.utils.fast.util.shorts.transform.booleans;

import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction;
import it.unimi.dsi.fastutil.booleans.BooleanListIterator;

public class Z2STransformingListIterator extends TransformingShortListIterator<Boolean, BooleanListIterator> {
    private final Boolean2ShortFunction transformer;

    public Z2STransformingListIterator(BooleanListIterator itr, Boolean2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.get(itr.nextBoolean());
    }

    @Override
    public short previousShort() {
        return transformer.get(itr.previousBoolean());
    }
}
