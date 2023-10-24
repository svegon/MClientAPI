package com.github.svegon.utils.fast.util.doubles.transform.shorts;

import com.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.ShortList;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoublePredicate;

public class S2DTransformingList extends TransformingDoubleSequentialList<Short, ShortList> {
    private final Short2DoubleFunction transformer;

    public S2DTransformingList(ShortList list, Short2DoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull DoubleListIterator listIterator(int index) {
        return new S2DTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public double removeDouble(int index) {
        return transformer.get(list.removeShort(index));
    }
}
