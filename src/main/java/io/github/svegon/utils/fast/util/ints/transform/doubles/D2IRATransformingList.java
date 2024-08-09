package io.github.svegon.utils.fast.util.ints.transform.doubles;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleList;

import java.util.function.DoubleToIntFunction;
import java.util.function.IntPredicate;

public class D2IRATransformingList extends TransformingIntRandomAccessList<Double, DoubleList> {
    private final DoubleToIntFunction transformer;

    public D2IRATransformingList(DoubleList list, DoubleToIntFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int getInt(int index) {
        return transformer.applyAsInt(list.getDouble(index));
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
            return list.removeIf(e -> filter.test(transformer.applyAsInt(e)));
    }
}
