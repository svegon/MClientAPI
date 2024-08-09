package io.github.svegon.utils.fast.util.objects.transform.objects;

import com.google.common.base.Preconditions;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.ShortIterator;

import java.util.Iterator;
import java.util.function.Function;

public class L2LTransformingIterator<T, E> extends TransformingObjectIterator<T, Iterator<T>, E> {
    private final Function<? super T, ? extends E> transformer;

    public L2LTransformingIterator(Iterator<T> itr, Function<? super T, ? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.apply(itr.next());
    }
}
