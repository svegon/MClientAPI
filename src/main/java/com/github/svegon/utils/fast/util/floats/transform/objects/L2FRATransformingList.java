package com.github.svegon.utils.fast.util.floats.transform.objects;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatRandomAccessList;
import com.github.svegon.utils.interfaces.function.Object2FloatFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatPredicate;

import java.util.List;

public class L2FRATransformingList<E> extends TransformingFloatRandomAccessList<E, List<E>> {
    private final Object2FloatFunction<? super E> transformer;

    public L2FRATransformingList(List<E> list, Object2FloatFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float getFloat(int index) {
        return transformer.applyAsFloat(list.get(index));
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsFloat(e)));
    }
}
