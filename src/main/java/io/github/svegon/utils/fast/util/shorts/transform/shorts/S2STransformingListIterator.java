package io.github.svegon.utils.fast.util.shorts.transform.shorts;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import it.unimi.dsi.fastutil.shorts.ShortUnaryOperator;

public class S2STransformingListIterator extends TransformingShortListIterator<Short, ShortListIterator> {
    private final ShortUnaryOperator transformer;

    public S2STransformingListIterator(ShortListIterator itr, ShortUnaryOperator transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.apply(itr.nextShort());
    }

    @Override
    public short previousShort() {
        return transformer.apply(itr.previousShort());
    }
}
