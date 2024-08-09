package io.github.svegon.utils.fast.util.chars.transform.ints;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.IntList;

public class I2CRATransformingList extends TransformingCharRandomAccessList<Integer, IntList> {
    private final Int2CharFunction transformer;

    public I2CRATransformingList(IntList list, Int2CharFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char getChar(int index) {
        return transformer.get(list.getInt(index));
    }

    @Override
    public boolean removeIf(final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
