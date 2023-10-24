package com.github.svegon.utils.fast.util.bytes.transform.bytes;

import com.github.svegon.utils.fast.util.bytes.transform.TransformingByteSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.bytes.ByteUnaryOperator;
import org.jetbrains.annotations.NotNull;

public class B2BTransformingList extends TransformingByteSequentialList<Byte, ByteList> {
    private final ByteUnaryOperator transformer;

    public B2BTransformingList(ByteList list, ByteUnaryOperator transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull ByteListIterator listIterator(int index) {
        return new B2BTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final BytePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.apply(e)));
    }

    @Override
    public byte removeByte(int index) {
        return transformer.apply(list.removeByte(index));
    }
}
