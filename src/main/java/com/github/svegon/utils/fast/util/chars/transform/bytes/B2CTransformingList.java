package com.github.svegon.utils.fast.util.chars.transform.bytes;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.chars.transform.TransformingCharSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.chars.CharListIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import org.jetbrains.annotations.NotNull;

public class B2CTransformingList extends TransformingCharSequentialList<Byte, ByteList> {
    private final Byte2CharFunction transformer;

    public B2CTransformingList(ByteList list, Byte2CharFunction transformer) {
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
        return transformer.get(list.removeByte(index));
    }
}
