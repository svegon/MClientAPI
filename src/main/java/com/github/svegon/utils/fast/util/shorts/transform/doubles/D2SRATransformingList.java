package com.github.svegon.utils.fast.util.shorts.transform.doubles;

import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortRandomAccessList;
import com.github.svegon.utils.interfaces.function.Object2ShortFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;

import java.util.List;

public class D2SRATransformingList extends TransformingShortRandomAccessList<Double, DoubleList> {
    private final Double2ShortFunction transformer;

    public D2SRATransformingList(DoubleList list, Double2ShortFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short getShort(int index) {
        return transformer.get(list.getDouble(index));
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
