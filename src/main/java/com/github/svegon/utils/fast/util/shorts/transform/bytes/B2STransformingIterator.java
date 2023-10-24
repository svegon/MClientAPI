package com.github.svegon.utils.fast.util.shorts.transform.bytes;

import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.bytes.ByteIterator;

public class B2STransformingIterator extends TransformingShortIterator<Byte, ByteIterator> {
    private final Byte2ShortFunction transformer;

    public B2STransformingIterator(ByteIterator itr, Byte2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.apply(itr.nextByte());
    }
}
