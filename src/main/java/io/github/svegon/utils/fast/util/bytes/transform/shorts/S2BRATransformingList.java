package io.github.svegon.utils.fast.util.bytes.transform.shorts;


import io.github.svegon.utils.fast.util.bytes.transform.TransformingByteRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.ShortList;

public class S2BRATransformingList extends TransformingByteRandomAccessList<Short, ShortList> {
    private final Short2ByteFunction transformer;

    public S2BRATransformingList(ShortList list, Short2ByteFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public byte getByte(int index) {
        return transformer.get(list.getShort(index));
    }

    @Override
    public boolean removeIf(final BytePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
