package io.github.svegon.utils.fast.util.ints.transform.booleans;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2IntFunction;
import it.unimi.dsi.fastutil.booleans.BooleanList;

import java.util.function.IntPredicate;

public class Z2IRATransformingList extends TransformingIntRandomAccessList<Boolean, BooleanList> {
    private final Boolean2IntFunction transformer;

    public Z2IRATransformingList(BooleanList list, Boolean2IntFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int getInt(int index) {
        return transformer.get(list.getBoolean(index));
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        return list.removeIf(e -> filter.test(transformer.get(e)));
    }
}
