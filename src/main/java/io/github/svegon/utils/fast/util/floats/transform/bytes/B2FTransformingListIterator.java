package io.github.svegon.utils.fast.util.floats.transform.bytes;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;

public class B2FTransformingListIterator extends TransformingFloatListIterator<Byte, ByteListIterator> {
    private final Byte2FloatFunction transformer;

    public B2FTransformingListIterator(ByteListIterator itr, Byte2FloatFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float nextFloat() {
        return transformer.get(itr.nextByte());
    }

    @Override
    public float previousFloat() {
        return transformer.get(itr.previousByte());
    }
}
