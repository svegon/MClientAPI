package io.github.svegon.utils.fast.util.doubles.transform.objects;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleIterator;
import com.google.common.base.Preconditions;

import java.util.Iterator;
import java.util.function.ToDoubleFunction;

public class L2DTransformingIterator<E> extends TransformingDoubleIterator<E, Iterator<E>> {
    private final ToDoubleFunction<? super E> transformer;

    public L2DTransformingIterator(Iterator<E> itr, ToDoubleFunction<? super E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.applyAsDouble(itr.next());
    }
}
