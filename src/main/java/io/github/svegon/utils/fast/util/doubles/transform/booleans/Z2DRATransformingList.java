package io.github.svegon.utils.fast.util.doubles.transform.booleans;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction;
import it.unimi.dsi.fastutil.booleans.BooleanList;

import java.util.function.DoublePredicate;

public class Z2DRATransformingList extends TransformingDoubleRandomAccessList<Boolean, BooleanList> {
    private final Boolean2DoubleFunction transformer;

    public Z2DRATransformingList(BooleanList list, Boolean2DoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double getDouble(int index) {
        return transformer.get(list.getBoolean(index));
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
