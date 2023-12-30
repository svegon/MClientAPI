package io.github.svegon.utils.fast.util.objects.transform.objects;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class L2LTransformingSet<T, E> extends TransformingObjectSet<T, Set<T>, E> {
    private final Function<? super T, ? extends E> forwardingTransformer;
    private final Function<? super E, ? extends T> backingTransformer;

    /**
     * the caller guarantees the forwarding and backing transformers map each
     * distinct original, respectively transformed element to a distinct
     * transformed, respectively original element
     *
     * @param set
     * @param forwardingTransformer
     * @param backingTransformer
     */
    public L2LTransformingSet(Set<T> set, Function<? super T, ? extends E> forwardingTransformer,
                              Function<? super E, ? extends T> backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ObjectIterator<E> iterator() {
        return new L2LTransformingIterator<>(set.iterator(), forwardingTransformer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        try {
            return set.contains(backingTransformer.apply((E) o));
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return set.removeIf(t -> filter.test(forwardingTransformer.apply(t)));
    }
}
