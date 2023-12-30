package io.github.svegon.utils.fast.util.bytes.transform.shorts;

import io.github.svegon.utils.fast.util.bytes.transform.TransformingByteListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;

public class S2BTransformingListIterator extends TransformingByteListIterator<Short, ShortListIterator> {
    private final Short2ByteFunction transformer;

    public S2BTransformingListIterator(ShortListIterator itr, Short2ByteFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public byte nextByte() {
        return transformer.get(itr.nextShort());
    }

    @Override
    public byte previousByte() {
        return transformer.get(itr.previousShort());
    }
}
