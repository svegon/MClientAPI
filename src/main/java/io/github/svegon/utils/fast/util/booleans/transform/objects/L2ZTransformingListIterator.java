package io.github.svegon.utils.fast.util.booleans.transform.objects;

import io.github.svegon.utils.fast.util.booleans.transform.TransformingBooleanListIterator;
import io.github.svegon.utils.interfaces.function.IntObjectBiPredicate;
import com.google.common.base.Preconditions;

import java.util.ListIterator;

public class L2ZTransformingListIterator<E> extends TransformingBooleanListIterator<E, ListIterator<E>> {
    private final IntObjectBiPredicate<? super E> transformer;

    public L2ZTransformingListIterator(ListIterator<E> itr, IntObjectBiPredicate<? super E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public boolean nextBoolean() {
        return transformer.test(nextIndex(), itr.next());
    }

    @Override
    public boolean previousBoolean() {
        return transformer.test(previousIndex(), itr.previous());
    }
}
