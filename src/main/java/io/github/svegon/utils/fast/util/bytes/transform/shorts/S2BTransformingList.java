package io.github.svegon.utils.fast.util.bytes.transform.shorts;

import io.github.svegon.utils.fast.util.bytes.transform.TransformingByteSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import it.unimi.dsi.fastutil.shorts.ShortList;
import org.jetbrains.annotations.NotNull;

public class S2BTransformingList extends TransformingByteSequentialList<Short, ShortList> {
    private final Short2ByteFunction transformer;

    public S2BTransformingList(ShortList list, Short2ByteFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull ByteListIterator listIterator(int index) {
        return new S2BTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final BytePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public byte removeByte(int index) {
        return transformer.get(list.removeShort(index));
    }
}
