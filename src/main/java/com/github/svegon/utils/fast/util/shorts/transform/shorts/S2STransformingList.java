package com.github.svegon.utils.fast.util.shorts.transform.shorts;

import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.ShortList;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;
import it.unimi.dsi.fastutil.shorts.ShortUnaryOperator;
import org.jetbrains.annotations.NotNull;

public class S2STransformingList extends TransformingShortSequentialList<Short, ShortList> {
    private final ShortUnaryOperator transformer;

    public S2STransformingList(ShortList list, ShortUnaryOperator transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull ShortListIterator listIterator(int index) {
        return new S2STransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.apply(e)));
    }

    @Override
    public short removeShort(int index) {
        return transformer.apply(list.removeShort(index));
    }
}
