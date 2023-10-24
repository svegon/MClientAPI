package com.github.svegon.utils.fast.util.doubles.transform.bytes;

import com.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;

public class B2DTransformingListIterator extends TransformingDoubleListIterator<Byte, ByteListIterator> {
    private final Byte2DoubleFunction transformer;

    public B2DTransformingListIterator(ByteListIterator itr, Byte2DoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.get(itr.nextByte());
    }

    @Override
    public double previousDouble() {
        return transformer.get(itr.previousByte());
    }
}
