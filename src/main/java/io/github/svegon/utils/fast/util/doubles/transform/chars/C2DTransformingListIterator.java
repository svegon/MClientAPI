package io.github.svegon.utils.fast.util.doubles.transform.chars;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.CharListIterator;

public class C2DTransformingListIterator extends TransformingDoubleListIterator<Character, CharListIterator> {
    private final Char2DoubleFunction transformer;

    public C2DTransformingListIterator(CharListIterator itr, Char2DoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.get(itr.nextChar());
    }

    @Override
    public double previousDouble() {
        return transformer.get(itr.previousChar());
    }
}
