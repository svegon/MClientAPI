package com.github.svegon.utils.fast.util.floats.transform.bytes;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.bytes.ByteIterator;

public class B2FTransformingIterator extends TransformingFloatIterator<Byte, ByteIterator> {
    private final Byte2FloatFunction transformer;

    protected B2FTransformingIterator(ByteIterator itr, Byte2FloatFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.get(itr.nextByte());
    }

    @Override
    public int skip(int n) {
        return itr.skip(n);
    }
}
