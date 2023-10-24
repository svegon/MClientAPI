package com.github.svegon.utils.fast.util.objects.transform.shorts;

import com.github.svegon.utils.fast.util.objects.transform.TransformingObjectRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.ShortList;

import java.util.function.Predicate;

public class S2LRATransformingList<E> extends TransformingObjectRandomAccessList<Short, ShortList, E> {
    private final Short2ObjectFunction<? extends E> transformer;

    public S2LRATransformingList(ShortList list, Short2ObjectFunction<? extends E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E get(int index) {
        return transformer.get(list.getShort(index));
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
