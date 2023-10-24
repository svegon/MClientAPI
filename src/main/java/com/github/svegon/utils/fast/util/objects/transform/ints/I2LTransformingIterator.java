package com.github.svegon.utils.fast.util.objects.transform.ints;

import com.github.svegon.utils.fast.util.objects.transform.TransformingObjectIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntIterator;

import java.util.function.IntFunction;

public class I2LTransformingIterator<E> extends TransformingObjectIterator<Integer, IntIterator, E> {
    private final IntFunction<? extends E> transformer;

    public I2LTransformingIterator(IntIterator itr, IntFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.apply(itr.nextInt());
    }
}
