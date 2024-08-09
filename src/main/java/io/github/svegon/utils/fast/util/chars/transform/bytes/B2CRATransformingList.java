package io.github.svegon.utils.fast.util.chars.transform.bytes;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.chars.CharPredicate;

public class B2CRATransformingList extends TransformingCharRandomAccessList<Byte, ByteList> {
    private final Byte2CharFunction transformer;

    public B2CRATransformingList(ByteList list, Byte2CharFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char getChar(int index) {
        return transformer.get(list.getByte(index));
    }

    @Override
    public boolean removeIf(final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
