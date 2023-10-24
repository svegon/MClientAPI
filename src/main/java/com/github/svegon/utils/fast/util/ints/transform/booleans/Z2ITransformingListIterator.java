package com.github.svegon.utils.fast.util.ints.transform.booleans;

import com.github.svegon.utils.fast.util.ints.transform.TransformingIntListIterator;
import it.unimi.dsi.fastutil.booleans.Boolean2IntFunction;
import it.unimi.dsi.fastutil.booleans.BooleanListIterator;

public class Z2ITransformingListIterator extends TransformingIntListIterator<Boolean, BooleanListIterator> {
    private final Boolean2IntFunction transformer;

    public Z2ITransformingListIterator(BooleanListIterator itr, Boolean2IntFunction transformer) {
        super(itr);
        this.transformer = transformer;
    }

    @Override
    public int previousInt() {
        return transformer.get(itr.previousBoolean());
    }

    @Override
    public int nextInt() {
        return transformer.get(itr.nextBoolean());
    }
}
