package io.github.svegon.utils.fast.util.chars.transform.objects;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharListIterator;
import io.github.svegon.utils.interfaces.function.Object2CharFunction;
import com.google.common.base.Preconditions;

import java.util.ListIterator;

public class L2CTransformingListIterator<E> extends TransformingCharListIterator<E, ListIterator<E>> {
    private final Object2CharFunction<? super E> transformer;

    public L2CTransformingListIterator(ListIterator<E> itr, Object2CharFunction<? super E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.applyAsChar(itr.next());
    }

    @Override
    public char previousChar() {
        return transformer.applyAsChar(itr.previous());
    }
}
