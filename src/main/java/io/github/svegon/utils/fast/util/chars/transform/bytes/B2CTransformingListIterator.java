package io.github.svegon.utils.fast.util.chars.transform.bytes;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharListIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;

public class B2CTransformingListIterator extends TransformingCharListIterator<Byte, ByteListIterator> {
    private final Byte2CharFunction transformer;

    public B2CTransformingListIterator(ByteListIterator itr, Byte2CharFunction transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char nextChar() {
        return transformer.get(itr.nextByte());
    }

    @Override
    public char previousChar() {
        return transformer.get(itr.previousByte());
    }
}
