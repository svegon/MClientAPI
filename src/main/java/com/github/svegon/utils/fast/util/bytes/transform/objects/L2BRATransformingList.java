package com.github.svegon.utils.fast.util.bytes.transform.objects;

import com.github.svegon.utils.fast.util.bytes.transform.TransformingByteRandomAccessList;
import com.github.svegon.utils.interfaces.function.Object2ByteFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.BytePredicate;

import java.util.List;

public class L2BRATransformingList<E> extends TransformingByteRandomAccessList<E, List<E>> {
    private final Object2ByteFunction<? super E> transformer;

    public L2BRATransformingList(List<E> list, Object2ByteFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public byte getByte(int index) {
        return transformer.applyAsByte(list.get(index));
    }

    @Override
    public boolean removeIf(final BytePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsByte(e)));
    }
}
