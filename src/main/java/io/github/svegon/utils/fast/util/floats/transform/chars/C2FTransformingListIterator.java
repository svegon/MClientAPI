package io.github.svegon.utils.fast.util.floats.transform.chars;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.CharListIterator;

public class C2FTransformingListIterator extends TransformingFloatListIterator<Character, CharListIterator> {
    private final Char2FloatFunction transformer;

    public C2FTransformingListIterator(CharListIterator itr, Char2FloatFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.get(itr.nextChar());
    }

    @Override
    public float previousFloat() {
        return transformer.get(itr.previousChar());
    }
}
