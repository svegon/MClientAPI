package io.github.svegon.utils.fast.util.shorts.transform.booleans;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.shorts.Short2BooleanFunction;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;

public class Z2STransformingMultiset extends TransformingShortMultiset<Boolean, BooleanCollection> {
    private final Boolean2ShortFunction forwardingTransformer;
    private final Short2BooleanFunction backingTransformer;

    public Z2STransformingMultiset(BooleanCollection c, Boolean2ShortFunction forwardingTransformer,
                                   Short2BooleanFunction backingTransformer) {
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
