package com.github.svegon.utils.fast.util.chars.transform.bytes;

import com.github.svegon.utils.fast.util.chars.transform.TransformingCharIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.ByteIterator;

public class B2CTransformingIterator extends TransformingCharIterator<Byte, ByteIterator> {
    private final Byte2CharFunction transformer;

    public B2CTransformingIterator(ByteIterator itr, Byte2CharFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.apply(itr.nextByte());
    }
}
