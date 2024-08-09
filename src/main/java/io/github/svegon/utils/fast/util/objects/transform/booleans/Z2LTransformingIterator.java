package io.github.svegon.utils.fast.util.objects.transform.booleans;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;

public class Z2LTransformingIterator<E> extends TransformingObjectIterator<Boolean, BooleanIterator, E> {
    private final Boolean2ObjectFunction<? extends E> transformer;

    public Z2LTransformingIterator(BooleanIterator itr, Boolean2ObjectFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.get(itr.nextBoolean());
    }
}
