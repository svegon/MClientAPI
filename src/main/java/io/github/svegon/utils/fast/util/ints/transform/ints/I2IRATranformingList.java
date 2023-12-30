package io.github.svegon.utils.fast.util.ints.transform.ints;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public class I2IRATranformingList extends TransformingIntRandomAccessList<Integer, IntList> {
    private final IntUnaryOperator transformer;

    public I2IRATranformingList(IntList list, IntUnaryOperator transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int getInt(int index) {
        return transformer.applyAsInt(list.getInt(index));
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        return list.removeIf(e -> filter.test(transformer.applyAsInt(e)));
    }
}
