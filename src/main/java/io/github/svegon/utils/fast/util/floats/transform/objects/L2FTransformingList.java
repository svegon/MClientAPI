package io.github.svegon.utils.fast.util.floats.transform.objects;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatSequentialList;
import io.github.svegon.utils.interfaces.function.Object2FloatFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatListIterator;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class L2FTransformingList<E> extends TransformingFloatSequentialList<E, List<E>> {
    private final Object2FloatFunction<? super E> transformer;

    public L2FTransformingList(List<E> list, Object2FloatFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull FloatListIterator listIterator(int index) {
        return new L2FTransformingListIterator<>(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsFloat(e)));
    }

    @Override
    public float removeFloat(int index) {
        return transformer.applyAsFloat(list.remove(index));
    }
}
