package io.github.svegon.utils.fast.util.objects.immutable;

import io.github.svegon.utils.ComparingUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public final class ImmutableObjectSingletonSortedSet<E> extends ImmutableNullSingletonSortedSet<E> {
    private final E element;

    ImmutableObjectSingletonSortedSet(E element, Comparator<? super E> comparator) {
        super(element.hashCode(), comparator);
        this.element = element;
    }

    @Override
    public boolean contains(@Nullable Object element) {
        return this.element.equals(element);
    }

    @Override
    public E first() {
        return element;
    }

    @Override
    public E last() {
        return element;
    }

    @Override
    public int compare(E o1, E o2) {
        return ComparingUtil.compare(comparator(), o1, o2);
    }
}
