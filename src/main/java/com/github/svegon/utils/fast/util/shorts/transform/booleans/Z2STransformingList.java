package com.github.svegon.utils.fast.util.shorts.transform.booleans;

import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;
import org.jetbrains.annotations.NotNull;

public class Z2STransformingList extends TransformingShortSequentialList<Boolean, BooleanList> {
    private final Boolean2ShortFunction transformer;

    public Z2STransformingList(BooleanList list, Boolean2ShortFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull ShortListIterator listIterator(int index) {
        return new Z2STransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public short removeShort(int index) {
        return transformer.get(list.removeBoolean(index));
    }
}
