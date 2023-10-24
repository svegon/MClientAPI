package com.github.svegon.utils.fast.util.doubles.transform.floats;

import com.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.FloatList;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoublePredicate;

public class F2DTransformingList extends TransformingDoubleSequentialList<Float, FloatList> {
    private final Float2DoubleFunction transformer;

    public F2DTransformingList(FloatList list, Float2DoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull DoubleListIterator listIterator(int index) {
        return new F2DTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public double removeDouble(int index) {
        return transformer.get(list.removeFloat(index));
    }
}
