package io.github.svegon.utils.fast.util.objects.transform.booleans;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectSet;
import io.github.svegon.utils.interfaces.function.Object2BooleanFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import it.unimi.dsi.fastutil.booleans.BooleanSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.function.Predicate;

public class Z2LTransformingSet<E> extends TransformingObjectSet<Boolean, BooleanSet, E> {
    private final Boolean2ObjectFunction<? extends E> forwardingTransformer;
    private final Object2BooleanFunction<? super E> backingTransformer;

    /**
     * the caller guarantees the forwarding and backing transformers map each
     * distinct original, respectively transformed element to a distinct
     * transformed, respectively original element
     *
     * @param set
     * @param forwardingTransformer
     * @param backingTransformer
     */
    public Z2LTransformingSet(BooleanSet set, Boolean2ObjectFunction<? extends E> forwardingTransformer,
                              Object2BooleanFunction<? super E> backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ObjectIterator<E> iterator() {
        return IterationUtil.mapToObj(set.iterator(), forwardingTransformer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        try {
            return set.contains(backingTransformer.applyToBoolean((E) o));
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return set.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
