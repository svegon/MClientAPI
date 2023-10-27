package io.github.svegon.utils.fast.util.floats.transform.chars;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.CharIterator;

public class C2FTransformingIterator extends TransformingFloatIterator<Character, CharIterator> {
    private final Char2FloatFunction transformer;

    protected C2FTransformingIterator(CharIterator itr, Char2FloatFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.get(itr.nextChar());
    }

    @Override
    public int skip(int n) {
        return itr.skip(n);
    }
}
