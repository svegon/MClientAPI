package io.github.svegon.utils.fast.util.doubles.transform.booleans;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;

public class Z2DTransformingIterator extends TransformingDoubleIterator<Boolean, BooleanIterator> {
    private final Boolean2DoubleFunction transformer;

    public Z2DTransformingIterator(BooleanIterator itr, Boolean2DoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.get(itr.nextBoolean());
    }
}
