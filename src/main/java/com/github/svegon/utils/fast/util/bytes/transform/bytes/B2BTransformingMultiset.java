package com.github.svegon.utils.fast.util.bytes.transform.bytes;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.bytes.transform.TransformingByteMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.*;

public class B2BTransformingMultiset extends TransformingByteMultiset<Byte, ByteCollection> {
    private final ByteUnaryOperator forwardingTransformer;
    private final ByteUnaryOperator backingTransformer;

    public B2BTransformingMultiset(ByteCollection c, ByteUnaryOperator forwardingTransformer,
                                   ByteUnaryOperator backingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ByteIterator iterator() {
        return IterationUtil.mapToByte(col.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(byte value) {
        return col.contains(backingTransformer.apply(value));
    }

    @Override
    public boolean removeIf(final BytePredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.apply(t)));
    }
}
