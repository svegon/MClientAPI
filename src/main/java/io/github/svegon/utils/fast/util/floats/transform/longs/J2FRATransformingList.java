package io.github.svegon.utils.fast.util.floats.transform.longs;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.LongList;

public class J2FRATransformingList extends TransformingFloatRandomAccessList<Long, LongList> {
    private final Long2FloatFunction transformer;

    public J2FRATransformingList(LongList list, Long2FloatFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public float getFloat(int index) {
        return transformer.get(list.getLong(index));
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
