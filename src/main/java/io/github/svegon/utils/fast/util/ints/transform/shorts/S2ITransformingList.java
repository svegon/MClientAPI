package io.github.svegon.utils.fast.util.ints.transform.shorts;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.ShortList;

import java.util.function.IntPredicate;

public class S2ITransformingList extends TransformingIntSequentialList<Short, ShortList> {
    private final Short2IntFunction transformer;

    public S2ITransformingList(ShortList list, Short2IntFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public IntListIterator listIterator(int index) {
        return new S2ITransformingListIterator(list.listIterator(), transformer);
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        Preconditions.checkNotNull(filter);

        return list.removeIf(e -> filter.test(transformer.get(e)));
    }

    @Override
    public int removeInt(int index) {
        return transformer.get(list.getShort(index));
    }
}
