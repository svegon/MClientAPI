package io.github.svegon.utils.fast.util.doubles.transform.booleans;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoublePredicate;

public class Z2DTransformingList extends TransformingDoubleSequentialList<Boolean, BooleanList> {
    private final Boolean2DoubleFunction transformer;

    public Z2DTransformingList(BooleanList list, Boolean2DoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull DoubleListIterator listIterator(int index) {
        return new Z2DTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public double removeDouble(int index) {
        return transformer.get(list.removeBoolean(index));
    }
}
