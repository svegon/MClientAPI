package io.github.svegon.utils.fast.util.doubles.transform.shorts;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.ShortList;

import java.util.function.DoublePredicate;

public class S2DRATransformingList extends TransformingDoubleRandomAccessList<Short, ShortList> {
    private final Short2DoubleFunction transformer;

    public S2DRATransformingList(ShortList list, Short2DoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double getDouble(int index) {
        return transformer.get(list.getShort(index));
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
