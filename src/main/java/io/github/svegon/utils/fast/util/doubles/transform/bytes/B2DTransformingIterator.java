package io.github.svegon.utils.fast.util.doubles.transform.bytes;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.ByteIterator;

public class B2DTransformingIterator extends TransformingDoubleIterator<Byte, ByteIterator> {
    private final Byte2DoubleFunction transformer;

    public B2DTransformingIterator(ByteIterator itr, Byte2DoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.get(itr.nextByte());
    }
}
