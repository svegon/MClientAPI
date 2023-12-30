package io.github.svegon.utils.fast.util.chars.transform.shorts;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.ShortBidirectionalIterator;

public class S2CTransformingBidirectionalIterator
        extends TransformingCharBidirectionalIterator<Short, ShortBidirectionalIterator> {
    private final Short2CharFunction transformer;

    public S2CTransformingBidirectionalIterator(ShortBidirectionalIterator itr, Short2CharFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.get(itr.nextShort());
    }

    @Override
    public char previousChar() {
        return transformer.get(itr.previousShort());
    }
}
