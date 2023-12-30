package io.github.svegon.utils.fast.util.objects.transform.objects;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectListIterator;
import io.github.svegon.utils.interfaces.function.IntObjectBiFunction;
import com.google.common.base.Preconditions;

import java.util.ListIterator;

public class L2LTransformingListIterator<T, E> extends TransformingObjectListIterator<T, ListIterator<T>, E> {
    private final IntObjectBiFunction<? super T, ? extends E> transformer;

    public L2LTransformingListIterator(ListIterator<T> itr, IntObjectBiFunction<? super T, ? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.apply(itr.nextIndex(), itr.next());
    }

    @Override
    public E previous() {
        return transformer.apply(itr.previousIndex(), itr.previous());
    }
}
