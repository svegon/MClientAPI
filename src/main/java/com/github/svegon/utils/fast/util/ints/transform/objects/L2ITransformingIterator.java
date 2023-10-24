package com.github.svegon.utils.fast.util.ints.transform.objects;

import com.github.svegon.utils.fast.util.ints.transform.TransformingIntIterator;
import com.google.common.base.Preconditions;

import java.util.Iterator;
import java.util.function.ToIntFunction;

public class L2ITransformingIterator<E> extends TransformingIntIterator<E, Iterator<E>> {
    private final ToIntFunction<? super E> transformer;

    public L2ITransformingIterator(Iterator<E> itr, ToIntFunction<? super E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int nextInt() {
        return transformer.applyAsInt(itr.next());
    }
}
