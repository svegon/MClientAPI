package com.github.svegon.utils.fast.util.shorts.transform.ints;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;

public class I2STransformingMultiset extends TransformingShortMultiset<Integer, IntCollection> {
    private final Int2ShortFunction forwardingTransformer;
    private final Short2IntFunction backingTransformer;

    public I2STransformingMultiset(IntCollection c, Int2ShortFunction forwardingTransformer,
                                   Short2IntFunction backingTransformer) {
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
