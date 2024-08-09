package io.github.svegon.utils.fast.util.objects.transform.objects;

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.function.Function;

public class L2LBackedTransformingMultiset<T, E> extends L2LTransformingMultiset<T, E> {
    private final Function<? super E, ? extends T> backingTransformer;

    public L2LBackedTransformingMultiset(Collection<T> c, Function<? super T, ? extends E> forwardingTransformer,
                                         Function<? super E, ? extends T> backingTransformer) {
        super(c, forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        try {
            return col.contains(backingTransformer.apply((E) o));
        } catch (ClassCastException e) {
            return false;
        }
    }

    public Function<? super E, ? extends T> getBackingTransformer() {
        return backingTransformer;
    }
}
