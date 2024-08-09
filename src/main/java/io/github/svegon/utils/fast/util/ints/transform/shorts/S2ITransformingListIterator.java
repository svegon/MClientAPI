package io.github.svegon.utils.fast.util.ints.transform.shorts;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntListIterator;
import it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;

public class S2ITransformingListIterator extends TransformingIntListIterator<Short, ShortListIterator> {
    private final Short2IntFunction transformer;

    public S2ITransformingListIterator(ShortListIterator itr, Short2IntFunction transformer) {
        super(itr);
        this.transformer = transformer;
    }

    @Override
    public int previousInt() {
        return transformer.get(itr.previousShort());
    }

    @Override
    public int nextInt() {
        return transformer.get(itr.nextShort());
    }
}
