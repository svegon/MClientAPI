package io.github.svegon.utils.fast.util.objects.transform.shorts;

import io.github.svegon.utils.collections.SetUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectSortedSet;
import io.github.svegon.utils.interfaces.function.Object2ShortFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.ShortSortedSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Predicate;

public class S2LTransformingSortedSet<E> extends TransformingObjectSortedSet<Short, ShortSortedSet, E> {
    private final Short2ObjectFunction<? extends E> forwardingTransformer;
    private final Object2ShortFunction<? super E> backingTransformer;

    public S2LTransformingSortedSet(final ShortSortedSet set,
                                    final Short2ObjectFunction<? extends E> forwardingTransformer,
                                    final Object2ShortFunction<? super E> backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ObjectBidirectionalIterator<E> iterator(E fromElement) {
        return IterationUtil.mapToObj(set.iterator(backingTransformer.applyAsShort(fromElement)),
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
            return set.contains(backingTransformer.applyAsShort((E) o));
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
                ? set.comparator().compare(backingTransformer.applyAsShort(o1), backingTransformer.applyAsShort(o2))
                : Short.compare(backingTransformer.applyAsShort(o1), backingTransformer.applyAsShort(o2));
    }

    @Override
    public @NotNull ObjectSortedSet<E> subSet(E fromElement, E toElement) {
        return SetUtil.mapToObj(set.subSet(backingTransformer.applyAsShort(fromElement),
                backingTransformer.applyAsShort(toElement)), forwardingTransformer, backingTransformer);
    }

    @Override
    public @NotNull ObjectSortedSet<E> headSet(E toElement) {
        return SetUtil.mapToObj(set.headSet(backingTransformer.applyAsShort(toElement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public @NotNull ObjectSortedSet<E> tailSet(E fromElement) {
        return SetUtil.mapToObj(set.tailSet(backingTransformer.applyAsShort(fromElement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public E last() {
        return forwardingTransformer.get(set.lastShort());
    }
}
