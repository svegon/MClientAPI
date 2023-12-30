package io.github.svegon.utils.fast.util.objects.transform.shorts;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.ShortList;

import java.util.function.Predicate;

public class S2LTransformingList<E> extends TransformingObjectSequentialList<Short, ShortList, E> {
    private final Short2ObjectFunction<? extends E> transformer;

    public S2LTransformingList(ShortList list, Short2ObjectFunction<? extends E> transformer) {
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
