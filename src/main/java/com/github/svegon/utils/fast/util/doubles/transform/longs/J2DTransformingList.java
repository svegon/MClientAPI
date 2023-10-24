package com.github.svegon.utils.fast.util.doubles.transform.longs;

import com.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import it.unimi.dsi.fastutil.longs.LongList;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoublePredicate;
import java.util.function.LongToDoubleFunction;

public class J2DTransformingList extends TransformingDoubleSequentialList<Long, LongList> {
    private final LongToDoubleFunction transformer;

    public J2DTransformingList(LongList list, LongToDoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull DoubleListIterator listIterator(int index) {
        return new J2DTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsDouble(e)));
    }

    @Override
    public double removeDouble(int index) {
        return transformer.applyAsDouble(list.removeLong(index));
    }
}
