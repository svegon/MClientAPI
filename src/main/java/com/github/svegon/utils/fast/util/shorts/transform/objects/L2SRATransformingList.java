package com.github.svegon.utils.fast.util.shorts.transform.objects;

import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortRandomAccessList;
import com.github.svegon.utils.interfaces.function.Object2ShortFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;

import java.util.List;

public class L2SRATransformingList<E> extends TransformingShortRandomAccessList<E, List<E>> {
    private final Object2ShortFunction<? super E> transformer;

    public L2SRATransformingList(List<E> list, Object2ShortFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short getShort(int index) {
        return transformer.applyAsShort(list.get(index));
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsShort(e)));
    }
}
