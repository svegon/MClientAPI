package io.github.svegon.utils.fast.util.longs.transform.objects;

import io.github.svegon.utils.fast.util.longs.transform.TransformingLongSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.LongListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.LongPredicate;
import java.util.function.ToLongFunction;

public class L2JTransformingList<E> extends TransformingLongSequentialList<E, List<E>> {
    private final ToLongFunction<? super E> transformer;

    public L2JTransformingList(List<E> list, ToLongFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull LongListIterator listIterator(int index) {
        return new L2JTransformingListIterator<>(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final LongPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsLong(e)));
    }

    @Override
    public long removeLong(int index) {
        return transformer.applyAsLong(list.remove(index));
    }
}
