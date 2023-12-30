package io.github.svegon.utils.fast.util.objects.transform.ints;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntListIterator;

import java.util.function.IntFunction;

public class I2LTransformingListIterator<E> extends TransformingObjectListIterator<Integer, IntListIterator, E> {
    private final IntFunction<? extends E> transformer;

    public I2LTransformingListIterator(IntListIterator itr, IntFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.apply(itr.nextInt());
    }

    @Override
    public E previous() {
        return transformer.apply(itr.previousInt());
    }
}
