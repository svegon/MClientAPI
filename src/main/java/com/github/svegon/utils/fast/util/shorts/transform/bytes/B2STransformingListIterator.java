package com.github.svegon.utils.fast.util.shorts.transform.bytes;

import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;

public class B2STransformingListIterator extends TransformingShortListIterator<Byte, ByteListIterator> {
    private final Byte2ShortFunction transformer;

    public B2STransformingListIterator(ByteListIterator itr, Byte2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.get(itr.nextByte());
    }

    @Override
    public short previousShort() {
        return transformer.get(itr.previousByte());
    }
}
