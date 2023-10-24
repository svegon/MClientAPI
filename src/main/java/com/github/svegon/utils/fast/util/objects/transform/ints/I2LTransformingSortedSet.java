package com.github.svegon.utils.fast.util.objects.transform.ints;

import com.github.svegon.utils.collections.SetUtil;
import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.objects.transform.TransformingObjectSortedSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class I2LTransformingSortedSet<E> extends TransformingObjectSortedSet<Integer, IntSortedSet, E> {
    private final IntFunction<? extends E> forwardingTransformer;
    private final ToIntFunction<? super E> backingTransformer;

    public I2LTransformingSortedSet(final IntSortedSet set,
                                    final IntFunction<? extends E> forwardingTransformer,
                                    final ToIntFunction<? super E> backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ObjectBidirectionalIterator<E> iterator(E fromElement) {
        return IterationUtil.mapToObj(set.iterator(backingTransformer.applyAsInt(fromElement)),
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
            return set.contains(backingTransformer.applyAsInt((E) o));
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(s -> filter.test(forwardingTransformer.apply(s)));
    }

    @Override
    public @Nullable Comparator<? super E> comparator() {
        return set.comparator() != null ? (o1, o2) -> set.comparator().compare(backingTransformer.applyAsInt(o1),
                backingTransformer.applyAsInt(o2)) : Comparator.comparingInt(backingTransformer);
    }

    @Override
    public @NotNull ObjectSortedSet<E> subSet(E fromElement, E toElement) {
        return SetUtil.mapToObj(set.subSet(backingTransformer.applyAsInt(fromElement),
                backingTransformer.applyAsInt(toElement)), forwardingTransformer, backingTransformer);
    }

    @Override
    public @NotNull ObjectSortedSet<E> headSet(E toElement) {
        return SetUtil.mapToObj(set.headSet(backingTransformer.applyAsInt(toElement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public @NotNull ObjectSortedSet<E> tailSet(E fromElement) {
        return SetUtil.mapToObj(set.tailSet(backingTransformer.applyAsInt(fromElement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public E last() {
        return forwardingTransformer.apply(set.lastInt());
    }
}
