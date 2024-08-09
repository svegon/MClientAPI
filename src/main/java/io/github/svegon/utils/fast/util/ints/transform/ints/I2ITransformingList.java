package io.github.svegon.utils.fast.util.ints.transform.ints;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;

import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public class I2ITransformingList extends TransformingIntSequentialList<Integer, IntList> {
    private final IntUnaryOperator transformer;

    public I2ITransformingList(IntList list, IntUnaryOperator transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public IntListIterator listIterator(int index) {
        return new I2ITransformingListIterator(list.listIterator(), transformer);
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        Preconditions.checkNotNull(filter);

        return list.removeIf(e -> filter.test(transformer.applyAsInt(e)));
    }

    @Override
    public int removeInt(int index) {
        return transformer.applyAsInt(list.getInt(index));
    }
}
