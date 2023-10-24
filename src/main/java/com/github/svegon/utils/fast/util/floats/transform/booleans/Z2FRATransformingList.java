package com.github.svegon.utils.fast.util.floats.transform.booleans;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.floats.FloatPredicate;

public class Z2FRATransformingList extends TransformingFloatRandomAccessList<Boolean, BooleanList> {
    private final Boolean2FloatFunction transformer;

    public Z2FRATransformingList(BooleanList list, Boolean2FloatFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float getFloat(int index) {
        return transformer.get(list.getBoolean(index));
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
