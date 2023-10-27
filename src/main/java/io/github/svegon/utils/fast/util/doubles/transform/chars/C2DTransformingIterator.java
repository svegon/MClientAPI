package io.github.svegon.utils.fast.util.doubles.transform.chars;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.CharIterator;

public class C2DTransformingIterator extends TransformingDoubleIterator<Character, CharIterator> {
    private final Char2DoubleFunction transformer;

    public C2DTransformingIterator(CharIterator itr, Char2DoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.get(itr.nextChar());
    }
}
