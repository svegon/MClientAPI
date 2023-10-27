package io.github.svegon.utils.fast.util.chars.transform.booleans;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2CharFunction;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.chars.CharPredicate;

public class Z2CRATransformingList extends TransformingCharRandomAccessList<Boolean, BooleanList> {
    private final Boolean2CharFunction transformer;

    public Z2CRATransformingList(BooleanList list, Boolean2CharFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char getChar(int index) {
        return transformer.get(list.getBoolean(index));
    }

    @Override
    public boolean removeIf(final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
