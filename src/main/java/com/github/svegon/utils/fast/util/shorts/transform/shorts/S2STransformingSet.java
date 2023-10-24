package com.github.svegon.utils.fast.util.shorts.transform.shorts;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.*;

public class S2STransformingSet extends TransformingShortSet<Short, ShortSet> {
    private final ShortUnaryOperator forwardingTransformer;
    private final ShortUnaryOperator backingTransformer;

    /**
     * the caller guarantees the forwarding and backing transformers map each
     * distinct original, respectively transformed element to a distinct
     * transformed, respectively original element
     *
     * @param set
     * @param forwardingTransformer
     * @param backingTransformer
     */
    public S2STransformingSet(ShortSet set, ShortUnaryOperator forwardingTransformer,
                              ShortUnaryOperator backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ShortIterator iterator() {
        return IterationUtil.mapToShort(set.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(short o) {
        return set.contains(backingTransformer.apply(o));
    }

    @Override
    public boolean removeIf(ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(t -> filter.test(forwardingTransformer.apply(t)));
    }
}
