package io.github.svegon.utils.fast.util.objects.immutable;

import io.github.svegon.utils.ComparingUtil;
import io.github.svegon.utils.hash.HashUtil;
import it.unimi.dsi.fastutil.objects.*;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Immutable
public abstract class ImmutableObjectCollection<E> implements ObjectCollection<E> {
    private final int hashCode;

    ImmutableObjectCollection(int hashCode) {
        this.hashCode = hashCode;
    }

    public ImmutableObjectList<E> toList() {
        return new ImmutableObjectArrayList<>(toArray());
    }

    public ImmutableObjectSet<E> toSet() {
        return toSortedSet(ComparingUtil.even());
    }

    public ImmutableObjectSortedSet<E> toSortedSet(@Nullable Comparator<? super E> comparator) {
        return ImmutableObjectSortedSet.of(new ObjectOpenCustomHashSet<>(this, HashUtil.defaultStrategy()),
                comparator);
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public final int hashCode() {
        return hashCode;
    }

    @Override
    public final ImmutableObjectCollection<E> clone() throws CloneNotSupportedException {
        return this;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        Iterator<E> it = iterator();
        E e = it.next();
        StringBuilder sb = new StringBuilder(4 * size()).append('[').append(e == this ? "(this collection)" : e);

        while (it.hasNext()) {
            e = it.next();
            sb.append(", ").append(e == this ? "(this collection)" : e);
        }

        return sb.append(']').toString();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public final boolean containsAll(@NotNull Collection<?> c) {
        return parallelStream().allMatch(c::contains);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean removeAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean add(E key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(final @Nullable Object element) {
        return element == null ? parallelStream().anyMatch(Objects::isNull)
                : parallelStream().anyMatch(element::equals);
    }

    @Override
    public final boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Object[] toArray() {
        return toArray(ObjectArrays.DEFAULT_EMPTY_ARRAY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T @NotNull [] toArray(@NotNull T[] a) {
        if (a.length < size()) {
            a = Arrays.copyOf(a, size());
        }

        ObjectIterator<E> it = iterator();

        for (int i = 0; i < size(); i++) {
            a[i] = (T) it.next();
        }

        return a;
    }
}
