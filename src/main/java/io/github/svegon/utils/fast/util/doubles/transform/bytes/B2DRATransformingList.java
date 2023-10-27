package io.github.svegon.utils.fast.util.doubles.transform.bytes;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.ByteList;

import java.util.function.DoublePredicate;

public class B2DRATransformingList extends TransformingDoubleRandomAccessList<Byte, ByteList> {
    private final Byte2DoubleFunction transformer;

    public B2DRATransformingList(ByteList list, Byte2DoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double getDouble(int index) {
        return transformer.get(list.getByte(index));
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
