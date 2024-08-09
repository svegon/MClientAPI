package io.github.svegon.utils.fast.util.ints.transform.shorts;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.ints.transform.TransformingIntMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntPredicate;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

public class S2ITransformingMultiset extends TransformingIntMultiset<Short, ShortCollection> {
    private final Short2IntFunction forwardingTransformer;
    private final Int2ShortFunction backingTransformer;

    public S2ITransformingMultiset(ShortCollection c, Short2IntFunction forwardingTransformer,
                                   Int2ShortFunction backingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public IntIterator iterator() {
        return IterationUtil.mapToInt(col.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(int value) {
        return col.contains(backingTransformer.get(value));
    }

    @Override
    public boolean removeIf(final IntPredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
