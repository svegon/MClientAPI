package io.github.svegon.utils.fast.util.floats.transform.floats;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.floats.FloatListIterator;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import org.jetbrains.annotations.NotNull;

public class F2FTransformingList extends TransformingFloatSequentialList<Float, FloatList> {
    private final FloatUnaryOperator transformer;

    public F2FTransformingList(FloatList list, FloatUnaryOperator transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull FloatListIterator listIterator(int index) {
        return new F2FTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.apply(e)));
    }

    @Override
    public float removeFloat(int index) {
        return transformer.apply(list.removeFloat(index));
    }
}
