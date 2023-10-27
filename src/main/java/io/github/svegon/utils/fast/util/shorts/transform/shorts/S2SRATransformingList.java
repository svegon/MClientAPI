package io.github.svegon.utils.fast.util.shorts.transform.shorts;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.ShortList;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;
import it.unimi.dsi.fastutil.shorts.ShortUnaryOperator;

public class S2SRATransformingList extends TransformingShortRandomAccessList<Short, ShortList> {
    private final ShortUnaryOperator transformer;

    public S2SRATransformingList(ShortList list, ShortUnaryOperator transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short getShort(int index) {
        return transformer.apply(list.getShort(index));
    }

    @Override
    public boolean removeIf(final ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.apply(e)));
    }
}
