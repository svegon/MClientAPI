package io.github.svegon.utils.fast.util.doubles.transform.doubles;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;

public class D2DTransformingList extends TransformingDoubleSequentialList<Double, DoubleList> {
    private final DoubleUnaryOperator transformer;

    public D2DTransformingList(DoubleList list, DoubleUnaryOperator transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull DoubleListIterator listIterator(int index) {
        return new D2DTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsDouble(e)));
    }

    @Override
    public double removeDouble(int index) {
        return transformer.applyAsDouble(list.removeDouble(index));
    }
}
