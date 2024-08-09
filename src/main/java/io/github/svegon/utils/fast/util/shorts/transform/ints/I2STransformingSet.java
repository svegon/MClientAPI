package io.github.svegon.utils.fast.util.shorts.transform.ints;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;

public class I2STransformingSet extends TransformingShortSet<Integer, IntSet> {
    private final Int2ShortFunction forwardingTransformer;
    private final Short2IntFunction backingTransformer;

    /**
     * the caller guarantees the forwarding and backing transformers map each
     * distinct original, respectively transformed element to a distinct
     * transformed, respectively original element
     *
     * @param set
     * @param forwardingTransformer
     * @param backingTransformer
     */
    public I2STransformingSet(IntSet set, Int2ShortFunction forwardingTransformer,
                              Short2IntFunction backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ShortIterator iterator() {
        return IterationUtil.mapToShort(set.iterator(), forwardingTransformer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(short o) {
        return set.contains(backingTransformer.get(o));
    }

    @Override
    public boolean removeIf(ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
