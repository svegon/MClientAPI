package io.github.svegon.utils.fast.util.ints.transform.ints;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntUnaryOperator;

public class I2ITransformingBidirectionalIterator
        extends TransformingIntBidirectionalIterator<Integer, IntBidirectionalIterator> {
    private final IntUnaryOperator transformer;

    protected I2ITransformingBidirectionalIterator(IntBidirectionalIterator itr, IntUnaryOperator transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int previousInt() {
        return transformer.apply(itr.nextInt());
    }

    @Override
    public int nextInt() {
        return transformer.apply(itr.nextInt());
    }

    public IntUnaryOperator getTransformer() {
        return transformer;
    }
}
