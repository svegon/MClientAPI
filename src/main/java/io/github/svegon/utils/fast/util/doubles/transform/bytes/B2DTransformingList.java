package io.github.svegon.utils.fast.util.doubles.transform.bytes;

import io.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoublePredicate;

public class B2DTransformingList extends TransformingDoubleSequentialList<Byte, ByteList> {
    private final Byte2DoubleFunction transformer;

    public B2DTransformingList(ByteList list, Byte2DoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull DoubleListIterator listIterator(int index) {
        return new B2DTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public double removeDouble(int index) {
        return transformer.get(list.removeByte(index));
    }
}
