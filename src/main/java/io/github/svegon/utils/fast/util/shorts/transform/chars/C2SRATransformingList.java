package io.github.svegon.utils.fast.util.shorts.transform.chars;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;

public class C2SRATransformingList extends TransformingShortRandomAccessList<Character, CharList> {
    private final Char2ShortFunction transformer;

    public C2SRATransformingList(CharList list, Char2ShortFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short getShort(int index) {
        return transformer.get(list.getChar(index));
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
