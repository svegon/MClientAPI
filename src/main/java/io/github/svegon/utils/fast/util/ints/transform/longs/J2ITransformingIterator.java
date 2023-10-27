package io.github.svegon.utils.fast.util.ints.transform.longs;


import io.github.svegon.utils.fast.util.ints.transform.TransformingIntIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import it.unimi.dsi.fastutil.longs.LongIterator;

public class J2ITransformingIterator extends TransformingIntIterator<Long, LongIterator> {
    private final Long2IntFunction transformer;

    public J2ITransformingIterator(LongIterator itr, Long2IntFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int nextInt() {
        return transformer.apply(itr.nextLong());
    }
}
