package io.github.svegon.utils.fast.util.shorts.transform.objects;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortMultiset;
import io.github.svegon.utils.interfaces.function.Object2ShortFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;

import java.util.Collection;

public class L2STransformingMultiset<T> extends TransformingShortMultiset<T, Collection<T>> {
    private final Object2ShortFunction<? super T> forwardingTransformer;
    private final Short2ObjectFunction<? extends T> backingTransformer;

    public L2STransformingMultiset(Collection<T> c, Object2ShortFunction<? super T> forwardingTransformer,
                                   Short2ObjectFunction<? extends T> backingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ShortIterator iterator() {
        return IterationUtil.transformToShort(col.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(short value) {
        return col.contains(backingTransformer.get(value));
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.applyAsShort(t)));
    }
}
