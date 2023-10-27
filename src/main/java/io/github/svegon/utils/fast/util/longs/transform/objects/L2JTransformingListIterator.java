package io.github.svegon.utils.fast.util.longs.transform.objects;

import io.github.svegon.utils.fast.util.longs.transform.TransformingLongListIterator;
import com.google.common.base.Preconditions;

import java.util.ListIterator;
import java.util.function.ToLongFunction;

public class L2JTransformingListIterator<E> extends TransformingLongListIterator<E, ListIterator<E>> {
    private final ToLongFunction<? super E> transformer;

    public L2JTransformingListIterator(ListIterator<E> itr, ToLongFunction<? super E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public long nextLong() {
        return transformer.applyAsLong(itr.next());
    }

    @Override
    public long previousLong() {
        return transformer.applyAsLong(itr.previous());
    }
}
