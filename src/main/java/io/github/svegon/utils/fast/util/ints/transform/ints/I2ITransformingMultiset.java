package io.github.svegon.utils.fast.util.ints.transform.ints;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.ints.transform.TransformingIntMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntPredicate;

import java.util.function.IntUnaryOperator;

public class I2ITransformingMultiset extends TransformingIntMultiset<Integer, IntCollection> {
    private final IntUnaryOperator forwardingTransformer;
    private final IntUnaryOperator backingTransformer;

    public I2ITransformingMultiset(IntCollection c, IntUnaryOperator forwardingTransformer,
                                   IntUnaryOperator backingTransformer) {
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
        return col.contains(backingTransformer.applyAsInt(value));
    }

    @Override
    public boolean removeIf(final IntPredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.applyAsInt(t)));
    }
}
