package io.github.svegon.utils.fast.util.ints.transform.booleans;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2IntFunction;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.ints.IntListIterator;

import java.util.function.IntPredicate;

public class Z2ITransformingList extends TransformingIntSequentialList<Boolean, BooleanList> {
    private final Boolean2IntFunction transformer;

    public Z2ITransformingList(BooleanList list, Boolean2IntFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public IntListIterator listIterator(int index) {
        return new Z2ITransformingListIterator(list.listIterator(), transformer);
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        Preconditions.checkNotNull(filter);

        return list.removeIf(e -> filter.test(transformer.get(e)));
    }

    @Override
    public int removeInt(int index) {
        return transformer.get(list.getBoolean(index));
    }
}
