package com.github.svegon.utils.fast.util.floats.transform.doubles;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatRandomAccessList;
import com.github.svegon.utils.interfaces.function.Object2FloatFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.floats.FloatPredicate;

import java.util.List;

public class D2FRATransformingList extends TransformingFloatRandomAccessList<Double, DoubleList> {
    private final Double2FloatFunction transformer;

    public D2FRATransformingList(DoubleList list, Double2FloatFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float getFloat(int index) {
        return transformer.get(list.getDouble(index));
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
