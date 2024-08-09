package io.github.svegon.utils.fast.util.booleans.transform.bytes;

import io.github.svegon.utils.fast.util.booleans.transform.TransformingBooleanListIterator;
import io.github.svegon.utils.interfaces.function.IntByteBiPredicate;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;

public class B2ZTransformingListIterator extends TransformingBooleanListIterator<Byte, ByteListIterator> {
    private final IntByteBiPredicate transformer;

    public B2ZTransformingListIterator(ByteListIterator itr, IntByteBiPredicate transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public boolean nextBoolean() {
        return transformer.test(nextIndex(), itr.nextByte());
    }

    @Override
    public boolean previousBoolean() {
        return transformer.test(previousIndex(), itr.previousByte());
    }
}
