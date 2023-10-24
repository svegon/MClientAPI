package com.github.svegon.utils.fast.util.longs.transform.doubles;

import com.github.svegon.utils.fast.util.longs.transform.TransformingLongRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleList;

import java.util.List;
import java.util.function.DoubleToLongFunction;
import java.util.function.LongPredicate;
import java.util.function.ToLongFunction;

public class D2JRATransformingList extends TransformingLongRandomAccessList<Double, DoubleList> {
    private final DoubleToLongFunction transformer;

    public D2JRATransformingList(DoubleList list, DoubleToLongFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public long getLong(int index) {
        return transformer.applyAsLong(list.getDouble(index));
    }

    @Override
    public boolean removeIf(final LongPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsLong(e)));
    }
}
