package io.github.svegon.utils.fast.util.bytes.transform.shorts;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.bytes.transform.TransformingByteMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

public class S2BTransformingMultiset extends TransformingByteMultiset<Short, ShortCollection> {
    private final Short2ByteFunction forwardingTransformer;
    private final Byte2ShortFunction backingTransformer;

    public S2BTransformingMultiset(ShortCollection c, Short2ByteFunction forwardingTransformer,
                                   Byte2ShortFunction backingTransformer) {
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
        return col.contains(backingTransformer.get(value));
    }

    @Override
    public boolean removeIf(final BytePredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
