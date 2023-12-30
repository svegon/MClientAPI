package io.github.svegon.utils.fast.util.ints.transform.chars;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntListIterator;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.CharListIterator;

public class C2ITransformingListIterator extends TransformingIntListIterator<Character, CharListIterator> {
    private final Char2IntFunction transformer;

    public C2ITransformingListIterator(CharListIterator itr, Char2IntFunction transformer) {
        super(itr);
        this.transformer = transformer;
    }

    @Override
    public int previousInt() {
        return transformer.get(itr.previousChar());
    }

    @Override
    public int nextInt() {
        return transformer.get(itr.nextChar());
    }
}
