package io.github.svegon.utils.fast.util.shorts.transform.objects;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortSequentialList;
import io.github.svegon.utils.interfaces.function.Object2ShortFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class L2STransformingList<E> extends TransformingShortSequentialList<E, List<E>> {
    private final Object2ShortFunction<? super E> transformer;

    public L2STransformingList(List<E> list, Object2ShortFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull ShortListIterator listIterator(int index) {
        return new L2STransformingListIterator<>(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsShort(e)));
    }

    @Override
    public short removeShort(int index) {
        return transformer.applyAsShort(list.remove(index));
    }
}
