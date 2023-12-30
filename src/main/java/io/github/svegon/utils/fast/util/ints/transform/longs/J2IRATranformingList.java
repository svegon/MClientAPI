package io.github.svegon.utils.fast.util.ints.transform.longs;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.LongList;

import java.util.function.IntPredicate;
import java.util.function.LongToIntFunction;

public class J2IRATranformingList extends TransformingIntRandomAccessList<Long, LongList> {
    private final LongToIntFunction transformer;

    public J2IRATranformingList(LongList list, LongToIntFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int getInt(int index) {
        return transformer.applyAsInt(list.getLong(index));
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        return list.removeIf(e -> filter.test(transformer.applyAsInt(e)));
    }
}
