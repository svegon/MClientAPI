package io.github.svegon.utils.fast.util.chars.transform.shorts;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.ShortList;
import it.unimi.dsi.fastutil.chars.CharPredicate;

public class S2CRATransformingList extends TransformingCharRandomAccessList<Short, ShortList> {
    private final Short2CharFunction transformer;

    public S2CRATransformingList(ShortList list, Short2CharFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char getChar(int index) {
        return transformer.get(list.getShort(index));
    }

    @Override
    public boolean removeIf(final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
