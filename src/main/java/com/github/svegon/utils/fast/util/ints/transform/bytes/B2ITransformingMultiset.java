package com.github.svegon.utils.fast.util.ints.transform.bytes;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.ints.transform.TransformingIntMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntPredicate;

public class B2ITransformingMultiset extends TransformingIntMultiset<Byte, ByteCollection> {
    private final Byte2IntFunction forwardingTransformer;
    private final Int2ByteFunction backingTransformer;

    public B2ITransformingMultiset(ByteCollection c, Byte2IntFunction forwardingTransformer,
                                   Int2ByteFunction backingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public IntIterator iterator() {
        return IterationUtil.mapToInt(col.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(int value) {
        return col.contains(backingTransformer.get(value));
    }

    @Override
    public boolean removeIf(final IntPredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
