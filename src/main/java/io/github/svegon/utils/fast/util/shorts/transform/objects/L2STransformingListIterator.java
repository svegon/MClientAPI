package io.github.svegon.utils.fast.util.shorts.transform.objects;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortListIterator;
import io.github.svegon.utils.interfaces.function.Object2ShortFunction;
import com.google.common.base.Preconditions;

import java.util.ListIterator;

public class L2STransformingListIterator<E> extends TransformingShortListIterator<E, ListIterator<E>> {
    private final Object2ShortFunction<? super E> transformer;

    public L2STransformingListIterator(ListIterator<E> itr, Object2ShortFunction<? super E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.applyAsShort(itr.next());
    }

    @Override
    public short previousShort() {
        return transformer.applyAsShort(itr.previous());
    }
}
