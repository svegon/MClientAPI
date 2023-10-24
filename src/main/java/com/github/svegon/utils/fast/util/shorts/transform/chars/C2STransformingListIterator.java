package com.github.svegon.utils.fast.util.shorts.transform.chars;

import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.chars.CharListIterator;

public class C2STransformingListIterator extends TransformingShortListIterator<Character, CharListIterator> {
    private final Char2ShortFunction transformer;

    public C2STransformingListIterator(CharListIterator itr, Char2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.get(itr.nextChar());
    }

    @Override
    public short previousShort() {
        return transformer.get(itr.previousChar());
    }
}
