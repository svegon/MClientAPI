package io.github.svegon.utils.fast.util.floats.transform.floats;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;

public class F2FRATransformingList extends TransformingFloatRandomAccessList<Float, FloatList> {
    private final FloatUnaryOperator transformer;

    public F2FRATransformingList(FloatList list, FloatUnaryOperator transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float getFloat(int index) {
        return transformer.apply(list.getFloat(index));
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.apply(e)));
    }
}
