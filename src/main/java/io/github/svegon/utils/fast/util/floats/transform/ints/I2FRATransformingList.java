package io.github.svegon.utils.fast.util.floats.transform.ints;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.IntList;

public class I2FRATransformingList extends TransformingFloatRandomAccessList<Integer, IntList> {
    private final Int2FloatFunction transformer;

    public I2FRATransformingList(IntList list, Int2FloatFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float getFloat(int index) {
        return transformer.get(list.getInt(index));
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
