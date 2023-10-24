package com.github.svegon.utils.fast.util.ints.transform.bytes;

import com.github.svegon.utils.fast.util.ints.transform.TransformingIntSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.ints.IntListIterator;

import java.util.function.IntPredicate;

public class B2ITransformingList extends TransformingIntSequentialList<Byte, ByteList> {
    private final Byte2IntFunction transformer;

    public B2ITransformingList(ByteList list, Byte2IntFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public IntListIterator listIterator(int index) {
        return new B2ITransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        Preconditions.checkNotNull(filter);

        return list.removeIf(e -> filter.test(transformer.get(e)));
    }

    @Override
    public int removeInt(int index) {
        return transformer.get(list.getByte(index));
    }
}
