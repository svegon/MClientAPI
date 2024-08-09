package io.github.svegon.utils.fast.util.ints.transform.objects;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

import java.util.function.ToIntFunction;

public class L2ITransformingBidirectionalIterator<E>
        extends TransformingIntBidirectionalIterator<E, ObjectBidirectionalIterator<E>> {
    private final ToIntFunction<? super E> transformer;

    public L2ITransformingBidirectionalIterator(ObjectBidirectionalIterator<E> itr,
                                                ToIntFunction<? super E> transformer) {
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
