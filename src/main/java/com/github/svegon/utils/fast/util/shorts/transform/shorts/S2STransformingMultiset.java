package com.github.svegon.utils.fast.util.shorts.transform.shorts;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortMultiset;
import com.github.svegon.utils.interfaces.function.Object2ShortFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.*;

import java.util.Collection;

public class S2STransformingMultiset extends TransformingShortMultiset<Short, ShortCollection> {
    private final ShortUnaryOperator forwardingTransformer;
    private final ShortUnaryOperator backingTransformer;

    public S2STransformingMultiset(ShortCollection c, ShortUnaryOperator forwardingTransformer,
                                   ShortUnaryOperator backingTransformer) {
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
        return col.contains(backingTransformer.apply(value));
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.apply(t)));
    }
}
