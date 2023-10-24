package com.github.svegon.utils.fast.util.doubles.transform.ints;

import com.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.function.DoublePredicate;
import java.util.function.IntToDoubleFunction;

public class I2DRATransformingList extends TransformingDoubleRandomAccessList<Integer, IntList> {
    private final IntToDoubleFunction transformer;

    public I2DRATransformingList(IntList list, IntToDoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double getDouble(int index) {
        return transformer.applyAsDouble(list.getInt(index));
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsDouble(e)));
    }
}
