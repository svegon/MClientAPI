package io.github.svegon.utils.fast.util.longs.transform.doubles;

import io.github.svegon.utils.fast.util.longs.transform.TransformingLongSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.longs.LongListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleToLongFunction;
import java.util.function.LongPredicate;

public class D2JTransformingList extends TransformingLongSequentialList<Double, DoubleList> {
    private final DoubleToLongFunction transformer;

    public D2JTransformingList(DoubleList list, DoubleToLongFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull LongListIterator listIterator(int index) {
        return new D2JTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final LongPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsLong(e)));
    }

    @Override
    public long removeLong(int index) {
        return transformer.applyAsLong(list.removeDouble(index));
    }
}
