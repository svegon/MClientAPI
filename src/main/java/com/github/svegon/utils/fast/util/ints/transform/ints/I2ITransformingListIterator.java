package com.github.svegon.utils.fast.util.ints.transform.ints;

import com.github.svegon.utils.fast.util.ints.transform.TransformingIntListIterator;
import it.unimi.dsi.fastutil.ints.IntListIterator;

import java.util.function.IntUnaryOperator;

public class I2ITransformingListIterator extends TransformingIntListIterator<Integer, IntListIterator> {
    private final IntUnaryOperator transformer;

    public I2ITransformingListIterator(IntListIterator itr, IntUnaryOperator transformer) {
        super(itr);
        this.transformer = transformer;
    }

    @Override
    public int previousInt() {
        return transformer.applyAsInt(itr.previousInt());
    }

    @Override
    public int nextInt() {
        return transformer.applyAsInt(itr.nextInt());
    }
}
