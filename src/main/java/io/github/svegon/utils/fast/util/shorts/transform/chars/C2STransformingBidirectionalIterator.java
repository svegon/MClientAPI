package io.github.svegon.utils.fast.util.shorts.transform.chars;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.chars.CharBidirectionalIterator;

public class C2STransformingBidirectionalIterator
        extends TransformingShortBidirectionalIterator<Character, CharBidirectionalIterator> {
    private final Char2ShortFunction transformer;

    public C2STransformingBidirectionalIterator(CharBidirectionalIterator itr, Char2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short previousShort() {
        return transformer.apply(itr.nextChar());
    }

    @Override
    public short nextShort() {
        return transformer.apply(itr.nextChar());
    }

    public Char2ShortFunction getTransformer() {
        return transformer;
    }
}
