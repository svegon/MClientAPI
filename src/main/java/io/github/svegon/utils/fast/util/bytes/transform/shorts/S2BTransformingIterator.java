package io.github.svegon.utils.fast.util.bytes.transform.shorts;

import io.github.svegon.utils.fast.util.bytes.transform.TransformingByteIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.ShortIterator;

public class S2BTransformingIterator extends TransformingByteIterator<Short, ShortIterator> {
    private final Short2ByteFunction transformer;

    public S2BTransformingIterator(ShortIterator itr, Short2ByteFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public byte nextByte() {
        return transformer.apply(itr.nextShort());
    }
}
