package io.github.svegon.utils.fast.util.chars.transform.chars;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharListIterator;
import it.unimi.dsi.fastutil.chars.CharUnaryOperator;

public class C2CTransformingListIterator extends TransformingCharListIterator<Character, CharListIterator> {
    private final CharUnaryOperator transformer;

    public C2CTransformingListIterator(CharListIterator itr, CharUnaryOperator transformer) {
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
