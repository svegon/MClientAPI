package io.github.svegon.utils.fast.util.objects.transform.bytes;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;

import java.util.function.Predicate;

public class B2LTransformingList<E> extends TransformingObjectSequentialList<Byte, ByteList, E> {
    private final Byte2ObjectFunction<? extends E> transformer;

    public B2LTransformingList(ByteList list, Byte2ObjectFunction<? extends E> transformer) {
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
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
