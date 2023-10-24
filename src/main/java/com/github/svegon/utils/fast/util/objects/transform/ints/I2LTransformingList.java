package com.github.svegon.utils.fast.util.objects.transform.ints;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.objects.transform.TransformingObjectSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;

import java.util.function.IntFunction;
import java.util.function.Predicate;

public class I2LTransformingList<E> extends TransformingObjectSequentialList<Integer, IntList, E> {
    private final IntFunction<? extends E> transformer;

    public I2LTransformingList(IntList list, IntFunction<? extends E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public ObjectListIterator<E> listIterator(int index) {
        return IterationUtil.mapToObj(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.apply(e)));
    }
}
