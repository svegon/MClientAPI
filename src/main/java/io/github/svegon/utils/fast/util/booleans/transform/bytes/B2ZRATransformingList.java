package io.github.svegon.utils.fast.util.booleans.transform.bytes;

import io.github.svegon.utils.fast.util.booleans.transform.TransformingBooleanRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.BooleanPredicate;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.bytes.BytePredicate;

public class B2ZRATransformingList extends TransformingBooleanRandomAccessList<Byte, ByteList> {
    private final BytePredicate transformer;

    public B2ZRATransformingList(ByteList list, BytePredicate transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public boolean getBoolean(int index) {
        return transformer.test(list.getByte(index));
    }

    @Override
    public boolean removeIf(final BooleanPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.test(e)));
    }
}
