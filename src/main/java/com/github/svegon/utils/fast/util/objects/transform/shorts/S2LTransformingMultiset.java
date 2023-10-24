package com.github.svegon.utils.fast.util.objects.transform.shorts;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.objects.transform.TransformingObjectMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

import java.util.function.Predicate;

public class S2LTransformingMultiset<E> extends TransformingObjectMultiset<Short, ShortCollection, E> {
    private final Short2ObjectFunction<? extends E> forwardingTransformer;
    private final Object2ShortFunction<? super E> backingTransformer;

    public S2LTransformingMultiset(ShortCollection c, Short2ObjectFunction<? extends E> forwardingTransformer,
                                   Object2ShortFunction<? super E> backingTransformer) {
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
        return col.contains(backingTransformer.getShort((E) o));
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
