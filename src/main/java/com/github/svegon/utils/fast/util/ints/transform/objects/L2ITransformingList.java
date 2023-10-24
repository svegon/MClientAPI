package com.github.svegon.utils.fast.util.ints.transform.objects;

import com.github.svegon.utils.fast.util.ints.transform.TransformingIntSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntListIterator;

import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.ToIntFunction;

public class L2ITransformingList<E> extends TransformingIntSequentialList<E, List<E>> {
    private final ToIntFunction<? super E> transformer;

    public L2ITransformingList(List<E> list, ToIntFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public IntListIterator listIterator(int index) {
        return new L2ITransformingListIterator<>(list.listIterator(), transformer);
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        Preconditions.checkNotNull(filter);

        return list.removeIf(e -> filter.test(transformer.applyAsInt(e)));
    }

    @Override
    public int removeInt(int index) {
        return transformer.applyAsInt(list.get(index));
    }
}
