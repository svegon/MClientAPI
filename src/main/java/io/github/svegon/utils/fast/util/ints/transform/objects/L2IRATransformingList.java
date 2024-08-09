package io.github.svegon.utils.fast.util.ints.transform.objects;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntRandomAccessList;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.ToIntFunction;

public class L2IRATransformingList<E> extends TransformingIntRandomAccessList<E, List<E>> {
    private final ToIntFunction<? super E> transformer;

    public L2IRATransformingList(List<E> list, ToIntFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int getInt(int index) {
        return transformer.applyAsInt(list.get(index));
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        return list.removeIf(e -> filter.test(transformer.applyAsInt(e)));
    }
}
