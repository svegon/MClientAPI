package io.github.svegon.utils.fast.util.doubles.transform.objects;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleRandomAccessList;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.function.DoublePredicate;
import java.util.function.ToDoubleFunction;

public class L2DRATransformingList<E> extends TransformingDoubleRandomAccessList<E, List<E>> {
    private final ToDoubleFunction<? super E> transformer;

    public L2DRATransformingList(List<E> list, ToDoubleFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double getDouble(int index) {
        return transformer.applyAsDouble(list.get(index));
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsDouble(e)));
    }
}
