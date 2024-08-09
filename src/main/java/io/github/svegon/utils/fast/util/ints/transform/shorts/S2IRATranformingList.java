package io.github.svegon.utils.fast.util.ints.transform.shorts;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.ShortList;

import java.util.function.IntPredicate;

public class S2IRATranformingList extends TransformingIntRandomAccessList<Short, ShortList> {
    private final Short2IntFunction transformer;

    public S2IRATranformingList(ShortList list, Short2IntFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int getInt(int index) {
        return transformer.get(list.getShort(index));
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        return list.removeIf(e -> filter.test(transformer.get(e)));
    }
}
