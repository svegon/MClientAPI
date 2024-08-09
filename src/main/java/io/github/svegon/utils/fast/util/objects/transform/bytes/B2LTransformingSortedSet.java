package io.github.svegon.utils.fast.util.objects.transform.bytes;

import io.github.svegon.utils.collections.SetUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectSortedSet;
import io.github.svegon.utils.interfaces.function.Object2ByteFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import it.unimi.dsi.fastutil.bytes.ByteSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Predicate;

public class B2LTransformingSortedSet<E> extends TransformingObjectSortedSet<Byte, ByteSortedSet, E> {
    private final Byte2ObjectFunction<? extends E> forwardingTransformer;
    private final Object2ByteFunction<? super E> backingTransformer;

    public B2LTransformingSortedSet(final ByteSortedSet set,
                                    final Byte2ObjectFunction<? extends E> forwardingTransformer,
                                    final Object2ByteFunction<? super E> backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ObjectBidirectionalIterator<E> iterator(E fromElement) {
        return IterationUtil.mapToObj(set.iterator(backingTransformer.applyAsByte(fromElement)),
                forwardingTransformer);
    }

    @Override
    public ObjectBidirectionalIterator<E> iterator() {
        return IterationUtil.mapToObj(set.iterator(), forwardingTransformer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        try {
            return set.contains(backingTransformer.applyAsByte((E) o));
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(s -> filter.test(forwardingTransformer.get(s)));
    }

    @Override
    public @Nullable Comparator<? super E> comparator() {
        return set.comparator() != null ? (o1, o2) -> set.comparator().compare(backingTransformer.applyAsByte(o1),
                backingTransformer.applyAsByte(o2)) : Comparator.comparingInt(backingTransformer::applyAsByte);
    }

    @Override
    public @NotNull ObjectSortedSet<E> subSet(E fromElement, E toElement) {
        return SetUtil.mapToObj(set.subSet(backingTransformer.applyAsByte(fromElement),
                backingTransformer.applyAsByte(toElement)), forwardingTransformer, backingTransformer);
    }

    @Override
    public @NotNull ObjectSortedSet<E> headSet(E toElement) {
        return SetUtil.mapToObj(set.headSet(backingTransformer.applyAsByte(toElement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public @NotNull ObjectSortedSet<E> tailSet(E fromElement) {
        return SetUtil.mapToObj(set.tailSet(backingTransformer.applyAsByte(fromElement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public E last() {
        return forwardingTransformer.get(set.lastByte());
    }
}
