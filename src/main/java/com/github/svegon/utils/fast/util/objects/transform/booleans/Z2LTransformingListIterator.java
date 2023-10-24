package com.github.svegon.utils.fast.util.objects.transform.booleans;

import com.github.svegon.utils.fast.util.objects.transform.TransformingObjectListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import it.unimi.dsi.fastutil.booleans.BooleanListIterator;

public class Z2LTransformingListIterator<E> extends TransformingObjectListIterator<Boolean, BooleanListIterator, E> {
    private final Boolean2ObjectFunction<? extends E> transformer;

    public Z2LTransformingListIterator(BooleanListIterator itr, Boolean2ObjectFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.get(itr.nextBoolean());
    }

    @Override
    public E previous() {
        return transformer.get(itr.previousBoolean());
    }
}
