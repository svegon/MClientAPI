package com.github.svegon.utils.fast.util.bytes.transform.objects;

import com.github.svegon.utils.fast.util.bytes.transform.TransformingByteListIterator;
import com.github.svegon.utils.interfaces.function.Object2ByteFunction;
import com.google.common.base.Preconditions;

import java.util.ListIterator;

public class L2BTransformingListIterator<E> extends TransformingByteListIterator<E, ListIterator<E>> {
    private final Object2ByteFunction<? super E> transformer;

    public L2BTransformingListIterator(ListIterator<E> itr, Object2ByteFunction<? super E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public byte nextByte() {
        return transformer.applyAsByte(itr.next());
    }

    @Override
    public byte previousByte() {
        return transformer.applyAsByte(itr.previous());
    }
}
