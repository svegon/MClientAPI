package com.github.svegon.utils.fast.util.chars.transform.booleans;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.chars.transform.TransformingCharMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2CharFunction;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.chars.Char2BooleanFunction;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;

public class Z2CTransformingMultiset extends TransformingCharMultiset<Boolean, BooleanCollection> {
    private final Boolean2CharFunction forwardingTransformer;
    private final Char2BooleanFunction backingTransformer;

    public Z2CTransformingMultiset(BooleanCollection c, Boolean2CharFunction forwardingTransformer,
                                   Char2BooleanFunction backingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public CharIterator iterator() {
        return IterationUtil.mapToChar(col.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(char value) {
        return col.contains(backingTransformer.get(value));
    }

    @Override
    public boolean removeIf(final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
