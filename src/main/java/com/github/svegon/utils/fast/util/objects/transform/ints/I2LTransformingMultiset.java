package com.github.svegon.utils.fast.util.objects.transform.ints;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.objects.transform.TransformingObjectMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class I2LTransformingMultiset<E> extends TransformingObjectMultiset<Integer, IntCollection, E> {
    private final IntFunction<? extends E> forwardingTransformer;
    private final ToIntFunction<? super E> backingTransformer;

    public I2LTransformingMultiset(IntCollection c, IntFunction<? extends E> forwardingTransformer,
                                   ToIntFunction<? super E> backingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ObjectIterator<E> iterator() {
        return IterationUtil.mapToObj(col.iterator(), forwardingTransformer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return col.contains(backingTransformer.applyAsInt((E) o));
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.apply(t)));
    }
}
