package io.github.svegon.utils.fast.util.bytes.transform.objects;

import io.github.svegon.utils.fast.util.bytes.transform.TransformingByteIterator;
import io.github.svegon.utils.interfaces.function.Object2ByteFunction;
import com.google.common.base.Preconditions;

import java.util.Iterator;

public class L2BTransformingIterator<E> extends TransformingByteIterator<E, Iterator<E>> {
    private final Object2ByteFunction<? super E> transformer;

    public L2BTransformingIterator(Iterator<E> itr, Object2ByteFunction<? super E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public byte nextByte() {
        return transformer.applyAsByte(itr.next());
    }
}
