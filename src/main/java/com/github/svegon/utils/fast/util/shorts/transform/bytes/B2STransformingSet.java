package com.github.svegon.utils.fast.util.shorts.transform.bytes;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;

public class B2STransformingSet extends TransformingShortSet<Byte, ByteSet> {
    private final Byte2ShortFunction forwardingTransformer;
    private final Short2ByteFunction backingTransformer;

    /**
     * the caller guarantees the forwarding and backing transformers map each
     * distinct original, respectively transformed element to a distinct
     * transformed, respectively original element
     *
     * @param set
     * @param forwardingTransformer
     * @param backingTransformer
     */
    public B2STransformingSet(ByteSet set, Byte2ShortFunction forwardingTransformer,
                              Short2ByteFunction backingTransformer) {
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
        return set.contains(backingTransformer.get(o));
    }

    @Override
    public boolean removeIf(ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
