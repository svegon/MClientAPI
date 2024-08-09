package io.github.svegon.utils.fast.util.chars.transform.objects;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharIterator;
import io.github.svegon.utils.interfaces.function.Object2CharFunction;
import com.google.common.base.Preconditions;

import java.util.Iterator;

public class L2CTransformingIterator<E> extends TransformingCharIterator<E, Iterator<E>> {
    private final Object2CharFunction<? super E> transformer;

    public L2CTransformingIterator(Iterator<E> itr, Object2CharFunction<? super E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.applyAsChar(itr.next());
    }
}
