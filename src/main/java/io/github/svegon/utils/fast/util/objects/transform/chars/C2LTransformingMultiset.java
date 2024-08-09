package io.github.svegon.utils.fast.util.objects.transform.chars;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.function.Predicate;

public class C2LTransformingMultiset<E> extends TransformingObjectMultiset<Character, CharCollection, E> {
    private final Char2ObjectFunction<? extends E> forwardingTransformer;
    private final Object2CharFunction<? super E> backingTransformer;

    public C2LTransformingMultiset(CharCollection c, Char2ObjectFunction<? extends E> forwardingTransformer,
                                   Object2CharFunction<? super E> backingTransformer) {
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
        return col.contains(backingTransformer.getChar((E) o));
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
