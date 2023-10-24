package com.github.svegon.utils.fast.util.objects.transform.booleans;

import com.github.svegon.utils.collections.SetUtil;
import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.booleans.BooleanSortedSet;
import com.github.svegon.utils.fast.util.objects.transform.TransformingObjectSortedSet;
import com.github.svegon.utils.interfaces.function.Object2BooleanFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Predicate;

public class Z2LTransformingSortedSet<E> extends TransformingObjectSortedSet<Boolean, BooleanSortedSet, E> {
    private final Boolean2ObjectFunction<? extends E> forwardingTransformer;
    private final Object2BooleanFunction<? super E> backingTransformer;

    public Z2LTransformingSortedSet(final BooleanSortedSet set,
                                    final Boolean2ObjectFunction<? extends E> forwardingTransformer,
                                    final Object2BooleanFunction<? super E> backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ObjectBidirectionalIterator<E> iterator(E fromElement) {
        return IterationUtil.mapToObj(set.iterator(backingTransformer.applyToBoolean(fromElement)),
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
            return set.contains(backingTransformer.applyToBoolean((E) o));
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
        return (o1, o2) -> set.comparator() != null
                ? set.comparator().compare(backingTransformer.applyToBoolean(o1), backingTransformer.applyToBoolean(o2))
                : Boolean.compare(backingTransformer.applyToBoolean(o1), backingTransformer.applyToBoolean(o2));
    }

    @Override
    public @NotNull ObjectSortedSet<E> subSet(E fromElement, E toElement) {
        return SetUtil.mapToObj(set.subSet(backingTransformer.applyToBoolean(fromElement),
                backingTransformer.applyToBoolean(toElement)), forwardingTransformer, backingTransformer);
    }

    @Override
    public @NotNull ObjectSortedSet<E> headSet(E toElement) {
        return SetUtil.mapToObj(set.headSet(backingTransformer.applyToBoolean(toElement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public @NotNull ObjectSortedSet<E> tailSet(E fromElement) {
        return SetUtil.mapToObj(set.tailSet(backingTransformer.applyToBoolean(fromElement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public E last() {
        return forwardingTransformer.get(set.lastBoolean());
    }
}
