package io.github.svegon.utils.fast.util.ints.transform.bytes;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import it.unimi.dsi.fastutil.bytes.ByteList;

import java.util.function.IntPredicate;

public class B2IRATransformingList extends TransformingIntRandomAccessList<Byte, ByteList> {
    private final Byte2IntFunction transformer;

    public B2IRATransformingList(ByteList list, Byte2IntFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int getInt(int index) {
        return transformer.get(list.getByte(index));
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
            return list.removeIf(e -> filter.test(transformer.get(e)));
    }
}
