package io.github.svegon.utils.fast.util.doubles.transform.shorts;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;

public class S2DTransformingListIterator extends TransformingDoubleListIterator<Short, ShortListIterator> {
    private final Short2DoubleFunction transformer;

    public S2DTransformingListIterator(ShortListIterator itr, Short2DoubleFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double nextDouble() {
        return transformer.get(itr.nextShort());
    }

    @Override
    public double previousDouble() {
        return transformer.get(itr.previousShort());
    }
}
