package io.github.svegon.utils.fast.util.objects.transform.bytes;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.bytes.ByteList;

import java.util.function.Predicate;

public class B2LRATransformingList<E> extends TransformingObjectRandomAccessList<Byte, ByteList, E> {
    private final Byte2ObjectFunction<? extends E> transformer;

    public B2LRATransformingList(ByteList list, Byte2ObjectFunction<? extends E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E get(int index) {
        return transformer.get(list.getByte(index));
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
