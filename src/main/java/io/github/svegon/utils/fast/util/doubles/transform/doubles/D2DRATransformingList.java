package io.github.svegon.utils.fast.util.doubles.transform.doubles;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleList;

import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;

public class D2DRATransformingList extends TransformingDoubleRandomAccessList<Double, DoubleList> {
    private final DoubleUnaryOperator transformer;

    public D2DRATransformingList(DoubleList list, DoubleUnaryOperator transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double getDouble(int index) {
        return transformer.applyAsDouble(list.getDouble(index));
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsDouble(e)));
    }
}
