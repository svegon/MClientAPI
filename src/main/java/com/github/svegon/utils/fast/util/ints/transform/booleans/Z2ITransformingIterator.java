package com.github.svegon.utils.fast.util.ints.transform.booleans;

import com.github.svegon.utils.fast.util.ints.transform.TransformingIntIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2IntFunction;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;

public class Z2ITransformingIterator extends TransformingIntIterator<Boolean, BooleanIterator> {
    private final Boolean2IntFunction transformer;

    public Z2ITransformingIterator(BooleanIterator itr, Boolean2IntFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int nextInt() {
        return transformer.apply(itr.nextBoolean());
    }
}
