package com.github.svegon.utils.fast.util.ints.transform.longs;

import com.github.svegon.utils.fast.util.ints.transform.TransformingIntListIterator;
import it.unimi.dsi.fastutil.longs.LongListIterator;

import java.util.function.LongToIntFunction;

public class J2ITransformingListIterator extends TransformingIntListIterator<Long, LongListIterator> {
    private final LongToIntFunction transformer;

    public J2ITransformingListIterator(LongListIterator itr, LongToIntFunction transformer) {
        super(itr);
        this.transformer = transformer;
    }

    @Override
    public int previousInt() {
        return transformer.applyAsInt(itr.previousLong());
    }

    @Override
    public int nextInt() {
        return transformer.applyAsInt(itr.nextLong());
    }
}
