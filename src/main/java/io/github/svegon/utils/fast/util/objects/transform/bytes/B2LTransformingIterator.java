package io.github.svegon.utils.fast.util.objects.transform.bytes;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.bytes.ByteIterator;

public class B2LTransformingIterator<E> extends TransformingObjectIterator<Byte, ByteIterator, E> {
    private final Byte2ObjectFunction<? extends E> transformer;

    public B2LTransformingIterator(ByteIterator itr, Byte2ObjectFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.get(itr.nextByte());
    }
}
