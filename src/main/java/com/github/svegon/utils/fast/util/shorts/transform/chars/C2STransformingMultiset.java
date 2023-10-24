package com.github.svegon.utils.fast.util.shorts.transform.chars;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;

public class C2STransformingMultiset extends TransformingShortMultiset<Character, CharCollection> {
    private final Char2ShortFunction forwardingTransformer;
    private final Short2CharFunction backingTransformer;

    public C2STransformingMultiset(CharCollection c, Char2ShortFunction forwardingTransformer,
                                   Short2CharFunction backingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ShortIterator iterator() {
        return IterationUtil.mapToShort(col.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(short value) {
        return col.contains(backingTransformer.get(value));
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
