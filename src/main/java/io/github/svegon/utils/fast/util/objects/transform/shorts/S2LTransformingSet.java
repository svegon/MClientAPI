package io.github.svegon.utils.fast.util.objects.transform.shorts;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectSet;
import io.github.svegon.utils.interfaces.function.Object2ShortFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.ShortSet;

import java.util.function.Predicate;

public class S2LTransformingSet<E> extends TransformingObjectSet<Short, ShortSet, E> {
    private final Short2ObjectFunction<? extends E> forwardingTransformer;
    private final Object2ShortFunction<? super E> backingTransformer;

    /**
     * the caller guarantees the forwarding and backing transformers map each
     * distinct original, respectively transformed element to a distinct
     * transformed, respectively original element
     *
     * @param set
     * @param forwardingTransformer
     * @param backingTransformer
     */
    public S2LTransformingSet(ShortSet set, Short2ObjectFunction<? extends E> forwardingTransformer,
                              Object2ShortFunction<? super E> backingTransformer) {
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
            return set.contains(backingTransformer.applyAsShort((E) o));
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return set.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
