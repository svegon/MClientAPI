package com.github.svegon.utils.fast.util.floats.transform.doubles;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.floats.FloatListIterator;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import org.jetbrains.annotations.NotNull;

public class D2FTransformingList extends TransformingFloatSequentialList<Double, DoubleList> {
    private final Double2FloatFunction transformer;

    public D2FTransformingList(DoubleList list, Double2FloatFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull FloatListIterator listIterator(int index) {
        return new D2FTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public float removeFloat(int index) {
        return transformer.get(list.removeDouble(index));
    }
}
