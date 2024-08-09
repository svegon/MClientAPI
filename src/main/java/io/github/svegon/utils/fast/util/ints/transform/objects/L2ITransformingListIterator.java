package io.github.svegon.utils.fast.util.ints.transform.objects;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntListIterator;
import com.google.common.base.Preconditions;

import java.util.ListIterator;
import java.util.function.ToIntFunction;

public class L2ITransformingListIterator<E> extends TransformingIntListIterator<E, ListIterator<E>> {
    private final ToIntFunction<? super E> transformer;

    public L2ITransformingListIterator(ListIterator<E> itr, ToIntFunction<? super E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int previousInt() {
        return transformer.applyAsInt(itr.previous());
    }

    @Override
    public int nextInt() {
        return transformer.applyAsInt(itr.next());
    }
}
