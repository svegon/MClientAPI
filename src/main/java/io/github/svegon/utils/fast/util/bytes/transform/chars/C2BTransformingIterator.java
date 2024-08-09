package io.github.svegon.utils.fast.util.bytes.transform.chars;

import io.github.svegon.utils.fast.util.bytes.transform.TransformingByteIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.CharIterator;

public class C2BTransformingIterator extends TransformingByteIterator<Character, CharIterator> {
    private final Char2ByteFunction transformer;

    public C2BTransformingIterator(CharIterator itr, Char2ByteFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public byte nextByte() {
        return transformer.apply(itr.nextChar());
    }
}
