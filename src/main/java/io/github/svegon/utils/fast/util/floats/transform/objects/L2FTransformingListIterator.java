package io.github.svegon.utils.fast.util.floats.transform.objects;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatListIterator;
import io.github.svegon.utils.interfaces.function.Object2FloatFunction;
import com.google.common.base.Preconditions;

import java.util.ListIterator;

public class L2FTransformingListIterator<E> extends TransformingFloatListIterator<E, ListIterator<E>> {
    private final Object2FloatFunction<? super E> transformer;

    public L2FTransformingListIterator(ListIterator<E> itr, Object2FloatFunction<? super E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.applyAsFloat(itr.next());
    }

    @Override
    public float previousFloat() {
        return transformer.applyAsFloat(itr.previous());
    }
}
