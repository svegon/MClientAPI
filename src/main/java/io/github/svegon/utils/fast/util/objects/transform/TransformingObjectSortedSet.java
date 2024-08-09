package io.github.svegon.utils.fast.util.objects.transform;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSpliterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.function.Predicate;

public abstract class TransformingObjectSortedSet<T, S extends SortedSet<T>, E>
        extends TransformingObjectSet<T, S, E> implements ObjectSortedSet<E> {
    protected TransformingObjectSortedSet(S set) {
        super(set);
    }

    @Override
    public abstract ObjectBidirectionalIterator<E> iterator(E fromElement);

    @Override
    public abstract ObjectBidirectionalIterator<E> iterator();

    @Override
    public ObjectSpliterator<E> spliterator() {
        return ObjectSortedSet.super.spliterator();
    }

    @Override
    public abstract boolean contains(Object o);

    @Override
    public abstract boolean removeIf(Predicate<? super E> filter);

    @Nullable
    @Override
    public abstract Comparator<? super E> comparator();

    @NotNull
    @Override
    public abstract ObjectSortedSet<E> subSet(E fromElement, E toElement);

    @NotNull
    @Override
    public abstract ObjectSortedSet<E> headSet(E toElement);

    @NotNull
    @Override
    public abstract ObjectSortedSet<E> tailSet(E fromElement);

    @Override
    public final E first() {
        return iterator().next();
    }

    @Override
    public abstract E last();
}
