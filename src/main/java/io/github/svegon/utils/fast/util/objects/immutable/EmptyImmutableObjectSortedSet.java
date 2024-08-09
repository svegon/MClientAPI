package io.github.svegon.utils.fast.util.objects.immutable;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.NoSuchElementException;

@Immutable
public class EmptyImmutableObjectSortedSet<E> extends ImmutableObjectSortedSet<E> {
    public static final EmptyImmutableObjectSortedSet DEFAULT = new EmptyImmutableObjectSortedSet(null) {
        @Override
        public int compare(Object o1, Object o2) {
            return ((Comparable) o1).compareTo(o2);
        }
    };

    private final Comparator<? super E> comparator;

    EmptyImmutableObjectSortedSet(Comparator<? super E> comparator) {
        super(0);
        this.comparator = comparator;
    }

    @Override
    public ImmutableObjectList<E> toList() {
        return ImmutableObjectArrayList.EMPTY;
    }

    @Override
    public ObjectBidirectionalIterator<E> iterator(E fromElement) {
        return ObjectIterators.EMPTY_ITERATOR;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ObjectBidirectionalIterator<E> iterator() {
        return ObjectIterators.EMPTY_ITERATOR;
    }

    @Nullable
    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public ImmutableObjectSortedSet<E> subSet(E fromElement, E toElement) {
        return this;
    }

    @Override
    public ImmutableObjectSortedSet<E> headSet(E toElement) {
        return this;
    }

    @Override
    public ImmutableObjectSortedSet<E> tailSet(E fromElement) {
        return this;
    }

    @Override
    public E first() {
        throw new NoSuchElementException();
    }

    @Override
    public E last() {
        throw new NoSuchElementException();
    }

    @Override
    public int compare(E o1, E o2) {
        return comparator().compare(o1, o2);
    }

    @Override
    public boolean contains(@Nullable Object element) {
        return false;
    }
}
