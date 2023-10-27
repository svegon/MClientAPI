package io.github.svegon.utils.fast.util.ints.transform.chars;


import io.github.svegon.utils.fast.util.ints.transform.TransformingIntIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.CharIterator;

public class C2ITransformingIterator extends TransformingIntIterator<Character, CharIterator> {
    private final Char2IntFunction transformer;

    public C2ITransformingIterator(CharIterator itr, Char2IntFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int nextInt() {
        return transformer.apply(itr.nextChar());
    }
}
