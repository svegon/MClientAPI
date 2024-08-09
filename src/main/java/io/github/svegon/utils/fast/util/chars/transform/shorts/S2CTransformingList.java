package io.github.svegon.utils.fast.util.chars.transform.shorts;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.chars.transform.TransformingCharSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.ShortList;
import it.unimi.dsi.fastutil.chars.CharListIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import org.jetbrains.annotations.NotNull;

public class S2CTransformingList extends TransformingCharSequentialList<Short, ShortList> {
    private final Short2CharFunction transformer;

    public S2CTransformingList(ShortList list, Short2CharFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull CharListIterator listIterator(int index) {
        return IterationUtil.mapToChar(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public char removeChar(int index) {
        return transformer.get(list.removeShort(index));
    }
}
