package io.github.svegon.utils.fast.util.doubles.transform.floats;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.FloatList;

import java.util.function.DoublePredicate;

public class F2DRATransformingList extends TransformingDoubleRandomAccessList<Float, FloatList> {
    private final Float2DoubleFunction transformer;

    public F2DRATransformingList(FloatList list, Float2DoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double getDouble(int index) {
        return transformer.get(list.getFloat(index));
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
