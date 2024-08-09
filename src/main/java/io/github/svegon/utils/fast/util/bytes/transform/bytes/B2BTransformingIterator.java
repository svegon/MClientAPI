package io.github.svegon.utils.fast.util.bytes.transform.bytes;

import io.github.svegon.utils.fast.util.bytes.transform.TransformingByteIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteUnaryOperator;

public class B2BTransformingIterator extends TransformingByteIterator<Byte, ByteIterator> {
    private final ByteUnaryOperator transformer;

    public B2BTransformingIterator(ByteIterator itr, ByteUnaryOperator transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public byte nextByte() {
        return transformer.apply(itr.nextByte());
    }
}
