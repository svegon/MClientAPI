package io.github.svegon.utils.fast.util.objects.transform.ints;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.function.IntFunction;
import java.util.function.Predicate;

public class I2LRATransformingList<E> extends TransformingObjectRandomAccessList<Integer, IntList, E> {
    private final IntFunction<? extends E> transformer;

    public I2LRATransformingList(IntList list, IntFunction<? extends E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E get(int index) {
        return transformer.apply(list.getInt(index));
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.apply(e)));
    }
}
