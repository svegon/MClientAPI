package io.github.svegon.utils.fast.util.chars.transform.booleans;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2CharFunction;
import it.unimi.dsi.fastutil.booleans.BooleanListIterator;

public class Z2CTransformingListIterator extends TransformingCharListIterator<Boolean, BooleanListIterator> {
    private final Boolean2CharFunction transformer;

    public Z2CTransformingListIterator(BooleanListIterator itr, Boolean2CharFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.get(itr.nextBoolean());
    }

    @Override
    public char previousChar() {
        return transformer.get(itr.previousBoolean());
    }
}
