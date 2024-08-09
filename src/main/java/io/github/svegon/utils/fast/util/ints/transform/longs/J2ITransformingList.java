package io.github.svegon.utils.fast.util.ints.transform.longs;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.ints.IntListIterator;

import java.util.function.IntPredicate;
import java.util.function.LongToIntFunction;

public class J2ITransformingList extends TransformingIntSequentialList<Long, LongList> {
    private final LongToIntFunction transformer;

    public J2ITransformingList(LongList list, LongToIntFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public IntListIterator listIterator(int index) {
        return new J2ITransformingListIterator(list.listIterator(), transformer);
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        Preconditions.checkNotNull(filter);

        return list.removeIf(e -> filter.test(transformer.applyAsInt(e)));
    }

    @Override
    public int removeInt(int index) {
        return transformer.applyAsInt(list.getLong(index));
    }
}
