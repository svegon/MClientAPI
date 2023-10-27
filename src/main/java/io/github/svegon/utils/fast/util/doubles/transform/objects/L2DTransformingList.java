package io.github.svegon.utils.fast.util.doubles.transform.objects;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.DoublePredicate;
import java.util.function.ToDoubleFunction;

public class L2DTransformingList<E> extends TransformingDoubleSequentialList<E, List<E>> {
    private final ToDoubleFunction<? super E> transformer;

    public L2DTransformingList(List<E> list, ToDoubleFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull DoubleListIterator listIterator(int index) {
        return new L2DTransformingListIterator<>(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsDouble(e)));
    }

    @Override
    public double removeDouble(int index) {
        return transformer.applyAsDouble(list.remove(index));
    }
}
