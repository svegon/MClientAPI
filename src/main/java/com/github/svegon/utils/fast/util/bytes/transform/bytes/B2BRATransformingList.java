package com.github.svegon.utils.fast.util.bytes.transform.bytes;


import com.github.svegon.utils.fast.util.bytes.transform.TransformingByteRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.bytes.ByteUnaryOperator;

public class B2BRATransformingList extends TransformingByteRandomAccessList<Byte, ByteList> {
    private final ByteUnaryOperator transformer;

    public B2BRATransformingList(ByteList list, ByteUnaryOperator transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public byte getByte(int index) {
        return transformer.apply(list.getByte(index));
    }

    @Override
    public boolean removeIf(final BytePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.apply(e)));
    }
}
