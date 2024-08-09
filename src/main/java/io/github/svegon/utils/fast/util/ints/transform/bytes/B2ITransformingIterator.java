package io.github.svegon.utils.fast.util.ints.transform.bytes;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.ByteIterator;

public class B2ITransformingIterator extends TransformingIntIterator<Byte, ByteIterator> {
    private final Byte2IntFunction transformer;

    public B2ITransformingIterator(ByteIterator itr, Byte2IntFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int nextInt() {
        return transformer.apply(itr.nextByte());
    }
}
