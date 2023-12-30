package io.github.svegon.utils.fast.util.objects.transform.bytes;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.function.Predicate;

public class B2LTransformingMultiset<E> extends TransformingObjectMultiset<Byte, ByteCollection, E> {
    private final Byte2ObjectFunction<? extends E> forwardingTransformer;
    private final Object2ByteFunction<? super E> backingTransformer;

    public B2LTransformingMultiset(ByteCollection c, Byte2ObjectFunction<? extends E> forwardingTransformer,
                                   Object2ByteFunction<? super E> backingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ObjectIterator<E> iterator() {
        return IterationUtil.mapToObj(col.iterator(), forwardingTransformer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return col.contains(backingTransformer.getByte((E) o));
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
