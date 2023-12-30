package io.github.svegon.utils.fast.util.chars.transform.booleans;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2CharFunction;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;

public class Z2CTransformingIterator extends TransformingCharIterator<Boolean, BooleanIterator> {
    private final Boolean2CharFunction transformer;

    public Z2CTransformingIterator(BooleanIterator itr, Boolean2CharFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.apply(itr.nextBoolean());
    }
}
