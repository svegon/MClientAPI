package io.github.svegon.utils.fast.util.doubles.transform.shorts;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.ShortIterator;

public class S2DTransformingIterator extends TransformingDoubleIterator<Short, ShortIterator> {
    private final Short2DoubleFunction transformer;

    public S2DTransformingIterator(ShortIterator itr, Short2DoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.get(itr.nextShort());
    }
}
