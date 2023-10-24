package com.github.svegon.utils.fast.util.floats.transform.bytes;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.ShortList;

public class B2FRATransformingList extends TransformingFloatRandomAccessList<Byte, ByteList> {
    private final Byte2FloatFunction transformer;

    public B2FRATransformingList(ByteList list, Byte2FloatFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float getFloat(int index) {
        return transformer.get(list.getByte(index));
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
