package io.github.svegon.utils.fast.util.doubles.transform.ints;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoublePredicate;
import java.util.function.IntToDoubleFunction;

public class I2DTransformingList extends TransformingDoubleSequentialList<Integer, IntList> {
    private final IntToDoubleFunction transformer;

    public I2DTransformingList(IntList list, IntToDoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull DoubleListIterator listIterator(int index) {
        return new I2DTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsDouble(e)));
    }

    @Override
    public double removeDouble(int index) {
        return transformer.applyAsDouble(list.removeInt(index));
    }
}
