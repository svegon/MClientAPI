package io.github.svegon.utils.fast.util.objects.transform.booleans;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import it.unimi.dsi.fastutil.booleans.BooleanList;

import java.util.function.Predicate;

public class Z2LRATransformingList<E> extends TransformingObjectRandomAccessList<Boolean, BooleanList, E> {
    private final Boolean2ObjectFunction<? extends E> transformer;

    public Z2LRATransformingList(BooleanList list, Boolean2ObjectFunction<? extends E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E get(int index) {
        return transformer.get(list.getBoolean(index));
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
