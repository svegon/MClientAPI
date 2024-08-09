package io.github.svegon.utils.fast.util.ints.transform.doubles;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntListIterator;

import java.util.function.DoubleToIntFunction;
import java.util.function.IntPredicate;

public class D2ITransformingList extends TransformingIntSequentialList<Double, DoubleList> {
    private final DoubleToIntFunction transformer;

    public D2ITransformingList(DoubleList list, DoubleToIntFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public IntListIterator listIterator(int index) {
        return new D2ITransformingListIterator(list.listIterator(), transformer);
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        Preconditions.checkNotNull(filter);

        return list.removeIf(e -> filter.test(transformer.applyAsInt(e)));
    }

    @Override
    public int removeInt(int index) {
        return transformer.applyAsInt(list.getDouble(index));
    }
}
