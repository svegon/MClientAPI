package com.github.svegon.utils.fast.util.chars.transform.ints;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.chars.transform.TransformingCharSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharListIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.NotNull;

public class I2CTransformingList extends TransformingCharSequentialList<Integer, IntList> {
    private final Int2CharFunction transformer;

    public I2CTransformingList(IntList list, Int2CharFunction transformer) {
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
        return transformer.get(list.removeInt(index));
    }
}
