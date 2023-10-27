package io.github.svegon.utils.fast.util.objects.transform.bytes;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;

public class B2LTransformingListIterator<E> extends TransformingObjectListIterator<Byte, ByteListIterator, E> {
    private final Byte2ObjectFunction<? extends E> transformer;

    public B2LTransformingListIterator(ByteListIterator itr, Byte2ObjectFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.get(itr.nextByte());
    }

    @Override
    public E previous() {
        return transformer.get(itr.previousByte());
    }
}
