package com.github.svegon.utils.fast.util.ints.transform.bytes;

import com.github.svegon.utils.fast.util.ints.transform.TransformingIntListIterator;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;

import java.util.function.DoubleToIntFunction;

public class B2ITransformingListIterator extends TransformingIntListIterator<Byte, ByteListIterator> {
    private final Byte2IntFunction transformer;

    public B2ITransformingListIterator(ByteListIterator itr, Byte2IntFunction transformer) {
        super(itr);
        this.transformer = transformer;
    }

    @Override
    public int previousInt() {
        return transformer.get(itr.previousByte());
    }

    @Override
    public int nextInt() {
        return transformer.get(itr.nextByte());
    }
}
