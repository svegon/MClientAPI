package com.github.svegon.utils.fast.util.floats.transform.bytes;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.floats.FloatListIterator;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import org.jetbrains.annotations.NotNull;

public class B2FTransformingList extends TransformingFloatSequentialList<Byte, ByteList> {
    private final Byte2FloatFunction transformer;

    public B2FTransformingList(ByteList list, Byte2FloatFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull FloatListIterator listIterator(int index) {
        return new B2FTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public float removeFloat(int index) {
        return transformer.get(list.removeByte(index));
    }
}
