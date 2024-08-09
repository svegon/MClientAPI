package io.github.svegon.utils.fast.util.objects.transform.booleans;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.function.Predicate;

public class Z2LTransformingMultiset<E> extends TransformingObjectMultiset<Boolean, BooleanCollection, E> {
    private final Boolean2ObjectFunction<? extends E> forwardingTransformer;
    private final Object2BooleanFunction<? super E> backingTransformer;

    public Z2LTransformingMultiset(BooleanCollection c, Boolean2ObjectFunction<? extends E> forwardingTransformer,
                                   Object2BooleanFunction<? super E> backingTransformer) {
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
        return col.contains(backingTransformer.getBoolean((E) o));
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
