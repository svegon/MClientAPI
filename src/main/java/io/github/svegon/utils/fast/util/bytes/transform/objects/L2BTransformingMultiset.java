package io.github.svegon.utils.fast.util.bytes.transform.objects;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.bytes.transform.TransformingByteMultiset;
import io.github.svegon.utils.interfaces.function.Object2ByteFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.BytePredicate;

import java.util.Collection;

public class L2BTransformingMultiset<T> extends TransformingByteMultiset<T, Collection<T>> {
    private final Object2ByteFunction<? super T> forwardingTransformer;
    private final Byte2ObjectFunction<? extends T> backingTransformer;

    public L2BTransformingMultiset(Collection<T> c, Object2ByteFunction<? super T> forwardingTransformer,
                                   Byte2ObjectFunction<? extends T> backingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ByteIterator iterator() {
        return IterationUtil.transformToByte(col.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(byte value) {
        return col.contains(backingTransformer.get(value));
    }

    @Override
    public boolean removeIf(final BytePredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.applyAsByte(t)));
    }
}
