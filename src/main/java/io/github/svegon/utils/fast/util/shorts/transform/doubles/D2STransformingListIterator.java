package io.github.svegon.utils.fast.util.shorts.transform.doubles;

import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;

public class D2STransformingListIterator extends TransformingShortListIterator<Double, DoubleListIterator> {
    private final Double2ShortFunction transformer;

    public D2STransformingListIterator(DoubleListIterator itr, Double2ShortFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public short nextShort() {
        return transformer.get(itr.nextDouble());
    }

    @Override
    public short previousShort() {
        return transformer.get(itr.previousDouble());
    }
}
