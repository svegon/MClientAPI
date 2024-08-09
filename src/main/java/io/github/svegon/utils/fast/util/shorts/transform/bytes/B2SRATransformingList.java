package io.github.svegon.utils.fast.util.shorts.transform.bytes;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;

public class B2SRATransformingList extends TransformingShortRandomAccessList<Byte, ByteList> {
    private final Byte2ShortFunction transformer;

    public B2SRATransformingList(ByteList list, Byte2ShortFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short getShort(int index) {
        return transformer.get(list.getByte(index));
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
