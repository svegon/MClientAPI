package io.github.svegon.utils.fast.util.objects.transform.booleans;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;

import java.util.function.Predicate;

public class Z2LTransformingList<E> extends TransformingObjectSequentialList<Boolean, BooleanList, E> {
    private final Boolean2ObjectFunction<? extends E> transformer;

    public Z2LTransformingList(BooleanList list, Boolean2ObjectFunction<? extends E> transformer) {
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
