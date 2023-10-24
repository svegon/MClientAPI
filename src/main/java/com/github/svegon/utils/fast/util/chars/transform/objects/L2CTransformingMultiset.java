package com.github.svegon.utils.fast.util.chars.transform.objects;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.chars.transform.TransformingCharMultiset;
import com.github.svegon.utils.interfaces.function.Object2CharFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;

import java.util.Collection;

public class L2CTransformingMultiset<T> extends TransformingCharMultiset<T, Collection<T>> {
    private final Object2CharFunction<? super T> forwardingTransformer;
    private final Char2ObjectFunction<? extends T> backingTransformer;

    public L2CTransformingMultiset(Collection<T> c, Object2CharFunction<? super T> forwardingTransformer,
                                   Char2ObjectFunction<? extends T> backingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public CharIterator iterator() {
        return IterationUtil.transformToChar(col.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(char value) {
        return col.contains(backingTransformer.get(value));
    }

    @Override
    public boolean removeIf(final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.applyAsChar(t)));
    }
}
