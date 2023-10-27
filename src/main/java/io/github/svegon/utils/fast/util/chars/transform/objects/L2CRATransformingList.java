package io.github.svegon.utils.fast.util.chars.transform.objects;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharRandomAccessList;
import io.github.svegon.utils.interfaces.function.Object2CharFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharPredicate;

import java.util.List;

public class L2CRATransformingList<E> extends TransformingCharRandomAccessList<E, List<E>> {
    private final Object2CharFunction<? super E> transformer;

    public L2CRATransformingList(List<E> list, Object2CharFunction<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char getChar(int index) {
        return transformer.applyAsChar(list.get(index));
    }

    @Override
    public boolean removeIf(final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.applyAsChar(e)));
    }
}
