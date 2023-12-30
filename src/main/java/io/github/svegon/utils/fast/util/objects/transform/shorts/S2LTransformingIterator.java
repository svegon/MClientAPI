package io.github.svegon.utils.fast.util.objects.transform.shorts;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.ShortIterator;

public class S2LTransformingIterator<E> extends TransformingObjectIterator<Short, ShortIterator, E> {
    private final Short2ObjectFunction<? extends E> transformer;

    public S2LTransformingIterator(ShortIterator itr, Short2ObjectFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.get(itr.nextShort());
    }
}
