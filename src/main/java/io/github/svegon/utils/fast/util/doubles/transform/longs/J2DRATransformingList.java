package io.github.svegon.utils.fast.util.doubles.transform.longs;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.LongList;

import java.util.function.DoublePredicate;
import java.util.function.LongToDoubleFunction;

public class J2DRATransformingList extends TransformingDoubleRandomAccessList<Long, LongList> {
    private final LongToDoubleFunction transformer;

    public J2DRATransformingList(LongList list, LongToDoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double getDouble(int index) {
        return transformer.applyAsDouble(list.getLong(index));
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsDouble(e)));
    }
}
