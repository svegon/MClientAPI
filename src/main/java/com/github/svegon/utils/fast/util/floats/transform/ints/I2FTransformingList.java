package com.github.svegon.utils.fast.util.floats.transform.ints;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatListIterator;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.NotNull;

public class I2FTransformingList extends TransformingFloatSequentialList<Integer, IntList> {
    private final Int2FloatFunction transformer;

    public I2FTransformingList(IntList list, Int2FloatFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull FloatListIterator listIterator(int index) {
        return new I2FTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public float removeFloat(int index) {
        return transformer.get(list.removeInt(index));
    }
}
