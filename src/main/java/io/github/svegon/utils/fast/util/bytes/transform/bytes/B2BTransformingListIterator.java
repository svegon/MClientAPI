package io.github.svegon.utils.fast.util.bytes.transform.bytes;

import io.github.svegon.utils.fast.util.bytes.transform.TransformingByteListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.bytes.ByteUnaryOperator;

public class B2BTransformingListIterator extends TransformingByteListIterator<Byte, ByteListIterator> {
    private final ByteUnaryOperator transformer;

    public B2BTransformingListIterator(ByteListIterator itr, ByteUnaryOperator transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public byte nextByte() {
        return transformer.apply(itr.nextByte());
    }

    @Override
    public byte previousByte() {
        return transformer.apply(itr.previousByte());
    }
}
