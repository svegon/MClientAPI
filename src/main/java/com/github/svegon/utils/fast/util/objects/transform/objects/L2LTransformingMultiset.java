package com.github.svegon.utils.fast.util.objects.transform.objects;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.objects.transform.TransformingObjectMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

public class L2LTransformingMultiset<T, E> extends TransformingObjectMultiset<T, Collection<T>, E> {
    private final Function<? super T, ? extends E> forwardingTransformer;

    public L2LTransformingMultiset(Collection<T> c, Function<? super T, ? extends E> forwardingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
    }

    @Override
    public ObjectIterator<E> iterator() {
        return IterationUtil.transformToObj(col.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(Object o) {
        ObjectIterator<E> it = iterator();

        if (o == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    return true;
                }
            }
        } else {
            while (it.hasNext()) {
                if (o.equals(it.next())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.apply(t)));
    }

    public Function<? super T, ? extends E> getForwardingTransformer() {
        return forwardingTransformer;
    }
}
