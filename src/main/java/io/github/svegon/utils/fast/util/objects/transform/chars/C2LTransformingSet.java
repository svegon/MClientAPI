package io.github.svegon.utils.fast.util.objects.transform.chars;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectSet;
import io.github.svegon.utils.interfaces.function.Object2CharFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.CharSet;

import java.util.function.Predicate;

public class C2LTransformingSet<E> extends TransformingObjectSet<Character, CharSet, E> {
    private final Char2ObjectFunction<? extends E> forwardingTransformer;
    private final Object2CharFunction<? super E> backingTransformer;

    /**
     * the caller guarantees the forwarding and backing transformers map each
     * distinct original, respectively transformed element to a distinct
     * transformed, respectively original element
     *
     * @param set
     * @param forwardingTransformer
     * @param backingTransformer
     */
    public C2LTransformingSet(CharSet set, Char2ObjectFunction<? extends E> forwardingTransformer,
                              Object2CharFunction<? super E> backingTransformer) {
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
            return set.contains(backingTransformer.applyAsChar((E) o));
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return set.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
