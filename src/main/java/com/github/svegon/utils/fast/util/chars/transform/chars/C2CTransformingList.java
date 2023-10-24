package com.github.svegon.utils.fast.util.chars.transform.chars;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.chars.transform.TransformingCharSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.chars.CharListIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import it.unimi.dsi.fastutil.chars.CharUnaryOperator;
import org.jetbrains.annotations.NotNull;

public class C2CTransformingList extends TransformingCharSequentialList<Character, CharList> {
    private final CharUnaryOperator transformer;

    public C2CTransformingList(CharList list, CharUnaryOperator transformer) {
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
        return list.removeIf((e) -> filter.test(transformer.apply(e)));
    }

    @Override
    public char removeChar(int index) {
        return transformer.apply(list.removeChar(index));
    }
}
