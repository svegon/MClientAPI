package com.github.svegon.utils.fast.util.shorts.transform.booleans;

import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;

public class Z2SRATransformingList extends TransformingShortRandomAccessList<Boolean, BooleanList> {
    private final Boolean2ShortFunction transformer;

    public Z2SRATransformingList(BooleanList list, Boolean2ShortFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short getShort(int index) {
        return transformer.get(list.getBoolean(index));
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
