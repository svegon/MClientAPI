package io.github.svegon.utils.fast.util.bytes.transform.chars;

import io.github.svegon.utils.fast.util.bytes.transform.TransformingByteListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.CharListIterator;

public class C2BTransformingListIterator extends TransformingByteListIterator<Character, CharListIterator> {
    private final Char2ByteFunction transformer;

    public C2BTransformingListIterator(CharListIterator itr, Char2ByteFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public byte nextByte() {
        return transformer.get(itr.nextChar());
    }

    @Override
    public byte previousByte() {
        return transformer.get(itr.previousChar());
    }
}
