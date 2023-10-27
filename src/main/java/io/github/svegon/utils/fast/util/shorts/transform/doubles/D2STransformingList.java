package io.github.svegon.utils.fast.util.shorts.transform.doubles;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;
import org.jetbrains.annotations.NotNull;

public class D2STransformingList extends TransformingShortSequentialList<Double, DoubleList> {
    private final Double2ShortFunction transformer;

    public D2STransformingList(DoubleList list, Double2ShortFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull ShortListIterator listIterator(int index) {
        return new D2STransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public short removeShort(int index) {
        return transformer.get(list.removeDouble(index));
    }
}
