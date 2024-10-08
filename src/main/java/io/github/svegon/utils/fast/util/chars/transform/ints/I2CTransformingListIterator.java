package io.github.svegon.utils.fast.util.chars.transform.ints;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.IntListIterator;

public class I2CTransformingListIterator extends TransformingCharListIterator<Integer, IntListIterator> {
    private final Int2CharFunction transformer;

    public I2CTransformingListIterator(IntListIterator itr, Int2CharFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.get(itr.nextInt());
    }

    @Override
    public char previousChar() {
        return transformer.get(itr.previousInt());
    }
}
