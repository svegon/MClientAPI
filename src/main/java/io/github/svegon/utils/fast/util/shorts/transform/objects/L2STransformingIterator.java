package io.github.svegon.utils.fast.util.shorts.transform.objects;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortIterator;
import io.github.svegon.utils.interfaces.function.Object2ShortFunction;
import com.google.common.base.Preconditions;

import java.util.Iterator;

public class L2STransformingIterator<E> extends TransformingShortIterator<E, Iterator<E>> {
    private final Object2ShortFunction<? super E> transformer;

    public L2STransformingIterator(Iterator<E> itr, Object2ShortFunction<? super E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.applyAsShort(itr.next());
    }
}
