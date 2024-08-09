package io.github.svegon.utils.fast.util.objects.immutable;

import io.github.svegon.utils.ComparingUtil;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public class ImmutableNullSingletonSortedSet<E> extends ImmutableObjectSortedSet<E> {
    public static final ImmutableNullSingletonSortedSet DEFAULT = new ImmutableNullSingletonSortedSet(null) {
        @Override
        public int compare(Object o1, Object o2) {
            return ComparingUtil.compare(o1, o2);
        }
    };

    private final Comparator<? super E> comparator;

    ImmutableNullSingletonSortedSet(int hashCode, Comparator<? super E> comparator) {
        super(hashCode);
        this.comparator = comparator;
    }

    ImmutableNullSingletonSortedSet(Comparator<? super E> comparator) {
        this(0, comparator);
    }

    @Override
    public final ObjectBidirectionalIterator<E> iterator(E fromElement) {
        ObjectBidirectionalIterator<E> it = iterator();

        if (compare(first(), fromElement) <= 0) {
            it.next();
        }

        return it;
    }

    @Override
    public final int size() {
        return 1;
    }

    @Override
    public final boolean isEmpty() {
        return false;
    }

    @Override
    public final ObjectBidirectionalIterator<E> iterator() {
        return ObjectIterators.singleton(first());
    }

    @Override
    public final ImmutableObjectSortedSet<E> subSet(E fromElement, E toElement) {
        if (compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException("fromElement > toElement");
        }

        if (compare(fromElement, first()) <= 0 && compare(last(), toElement) < 0) {
            return this;
        }

        return ImmutableObjectSortedSet.of(comparator());
    }

    @Override
    public final ImmutableObjectSortedSet<E> headSet(E toElement) {
        if (compare(last(), toElement) < 0) {
            return this;
        }

        return ImmutableObjectSortedSet.of(comparator());
    }

    @Override
    public final ImmutableObjectSortedSet<E> tailSet(E fromElement) {
        if (compare(fromElement, first()) <= 0) {
            return this;
        }

        return ImmutableObjectSortedSet.of(comparator());
    }

    @Override
    public E first() {
        return null;
    }

    @Override
    public E last() {
        return null;
    }

    @Override
    public final @Nullable Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public boolean contains(@Nullable Object element) {
        return element == null;
    }

    @Override
    public int compare(E o1, E o2) {
        return comparator().compare(o1, o2);
    }
}
