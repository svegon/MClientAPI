package io.github.svegon.utils.fast.util.chars.transform.objects;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharSequentialList;
import io.github.svegon.utils.interfaces.function.Object2CharFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharListIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class L2CTransformingList<E> extends TransformingCharSequentialList<E, List<E>> {
    private final Object2CharFunction<? super E> transformer;

    public L2CTransformingList(List<E> list, Object2CharFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull CharListIterator listIterator(int index) {
        return new L2CTransformingListIterator<>(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsChar(e)));
    }

    @Override
    public char removeChar(int index) {
        return transformer.applyAsChar(list.remove(index));
    }
}
