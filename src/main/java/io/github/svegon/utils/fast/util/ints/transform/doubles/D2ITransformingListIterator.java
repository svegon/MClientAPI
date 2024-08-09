package io.github.svegon.utils.fast.util.ints.transform.doubles;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntListIterator;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;

import java.util.function.DoubleToIntFunction;

public class D2ITransformingListIterator extends TransformingIntListIterator<Double, DoubleListIterator> {
    private final DoubleToIntFunction transformer;

    public D2ITransformingListIterator(DoubleListIterator itr, DoubleToIntFunction transformer) {
        super(itr);
        this.transformer = transformer;
    }

    @Override
    public int previousInt() {
        return transformer.applyAsInt(itr.previousDouble());
    }

    @Override
    public int nextInt() {
        return transformer.applyAsInt(itr.nextDouble());
    }
}
