package io.github.svegon.utils.fast.util.doubles.transform.objects;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleListIterator;
import com.google.common.base.Preconditions;

import java.util.ListIterator;
import java.util.function.ToDoubleFunction;

public class L2DTransformingListIterator<E> extends TransformingDoubleListIterator<E, ListIterator<E>> {
    private final ToDoubleFunction<? super E> transformer;

    public L2DTransformingListIterator(ListIterator<E> itr, ToDoubleFunction<? super E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.applyAsDouble(itr.next());
    }

    @Override
    public double previousDouble() {
        return transformer.applyAsDouble(itr.previous());
    }
}
