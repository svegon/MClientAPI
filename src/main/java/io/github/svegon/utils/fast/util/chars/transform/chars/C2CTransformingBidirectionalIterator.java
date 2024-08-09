package io.github.svegon.utils.fast.util.chars.transform.chars;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharBidirectionalIterator;
import it.unimi.dsi.fastutil.chars.CharUnaryOperator;

public class C2CTransformingBidirectionalIterator
        extends TransformingCharBidirectionalIterator<Character, CharBidirectionalIterator> {
    private final CharUnaryOperator transformer;

    public C2CTransformingBidirectionalIterator(CharBidirectionalIterator itr, CharUnaryOperator transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.apply(itr.nextChar());
    }

    @Override
    public char previousChar() {
        return transformer.apply(itr.previousChar());
    }
}
