package com.github.svegon.utils.fast.util.objects.transform.shorts;

import com.github.svegon.utils.fast.util.objects.transform.TransformingObjectListIterator;
import com.github.svegon.utils.interfaces.function.IntObjectBiFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;

import java.util.ListIterator;

public class S2LTransformingListIterator<E> extends TransformingObjectListIterator<Short, ShortListIterator, E> {
    private final Short2ObjectFunction<? extends E> transformer;

    public S2LTransformingListIterator(ShortListIterator itr, Short2ObjectFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.get(itr.nextShort());
    }

    @Override
    public E previous() {
        return transformer.get(itr.previousShort());
    }
}
