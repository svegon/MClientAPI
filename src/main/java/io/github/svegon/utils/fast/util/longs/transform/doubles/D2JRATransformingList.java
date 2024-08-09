package io.github.svegon.utils.fast.util.longs.transform.doubles;

import io.github.svegon.utils.fast.util.longs.transform.TransformingLongRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleList;

import java.util.function.DoubleToLongFunction;
import java.util.function.LongPredicate;

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
