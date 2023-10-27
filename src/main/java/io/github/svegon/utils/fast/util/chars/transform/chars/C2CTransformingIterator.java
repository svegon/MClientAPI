package io.github.svegon.utils.fast.util.chars.transform.chars;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharUnaryOperator;

public class C2CTransformingIterator extends TransformingCharIterator<Character, CharIterator> {
    private final CharUnaryOperator transformer;

    public C2CTransformingIterator(CharIterator itr, CharUnaryOperator transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.apply(itr.nextChar());
    }
}
