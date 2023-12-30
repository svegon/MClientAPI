package io.github.svegon.utils.fast.util.chars.transform.ints;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.IntIterator;

public class I2CTransformingIterator extends TransformingCharIterator<Integer, IntIterator> {
    private final Int2CharFunction transformer;

    public I2CTransformingIterator(IntIterator itr, Int2CharFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.apply(itr.nextInt());
    }
}
