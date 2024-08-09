package io.github.svegon.utils.fast.util.ints.transform.chars;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.CharListIterator;

public class C2ITransformingBidirectionalIterator
        extends TransformingIntBidirectionalIterator<Character, CharListIterator> {
    private final Char2IntFunction transformer;

    public C2ITransformingBidirectionalIterator(CharListIterator itr, Char2IntFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int previousInt() {
        return transformer.get(itr.previousChar());
    }

    @Override
    public int nextInt() {
        return transformer.get(itr.nextChar());
    }


    public Char2IntFunction getTransformer() {
        return transformer;
    }
}
