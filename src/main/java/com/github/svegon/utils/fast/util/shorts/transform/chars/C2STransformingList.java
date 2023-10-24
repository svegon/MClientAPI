package com.github.svegon.utils.fast.util.shorts.transform.chars;

import com.github.svegon.utils.fast.util.shorts.transform.TransformingShortSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;
import org.jetbrains.annotations.NotNull;

public class C2STransformingList extends TransformingShortSequentialList<Character, CharList> {
    private final Char2ShortFunction transformer;

    public C2STransformingList(CharList list, Char2ShortFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull ShortListIterator listIterator(int index) {
        return new C2STransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public short removeShort(int index) {
        return transformer.get(list.removeChar(index));
    }
}
