package io.github.svegon.utils.fast.util.shorts.transform.booleans;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;

public class Z2STransformingIterator extends TransformingShortIterator<Boolean, BooleanIterator> {
    private final Boolean2ShortFunction transformer;

    public Z2STransformingIterator(BooleanIterator itr, Boolean2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.apply(itr.nextBoolean());
    }
}
