package com.github.svegon.utils.fast.util.bytes.transform.objects;

import com.github.svegon.utils.fast.util.bytes.transform.TransformingByteSequentialList;
import com.github.svegon.utils.interfaces.function.Object2ByteFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class L2BTransformingList<E> extends TransformingByteSequentialList<E, List<E>> {
    private final Object2ByteFunction<? super E> transformer;

    public L2BTransformingList(List<E> list, Object2ByteFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull ByteListIterator listIterator(int index) {
        return new L2BTransformingListIterator<>(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final BytePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsByte(e)));
    }

    @Override
    public byte removeByte(int index) {
        return transformer.applyAsByte(list.remove(index));
    }
}
