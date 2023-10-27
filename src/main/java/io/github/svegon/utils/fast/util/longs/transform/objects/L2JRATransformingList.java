package io.github.svegon.utils.fast.util.longs.transform.objects;

import io.github.svegon.utils.fast.util.longs.transform.TransformingLongRandomAccessList;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.function.LongPredicate;
import java.util.function.ToLongFunction;

public class L2JRATransformingList<E> extends TransformingLongRandomAccessList<E, List<E>> {
    private final ToLongFunction<? super E> transformer;

    public L2JRATransformingList(List<E> list, ToLongFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public long getLong(int index) {
        return transformer.applyAsLong(list.get(index));
    }

    @Override
    public boolean removeIf(final LongPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsLong(e)));
    }
}
