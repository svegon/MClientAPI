package io.github.svegon.utils.fast.util.shorts.transform.chars;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.chars.CharIterator;

public class C2STransformingIterator extends TransformingShortIterator<Character, CharIterator> {
    private final Char2ShortFunction transformer;

    public C2STransformingIterator(CharIterator itr, Char2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.apply(itr.nextChar());
    }
}
