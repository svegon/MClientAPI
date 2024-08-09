package io.github.svegon.utils.fast.util.booleans.immutable;

import io.github.svegon.utils.fast.util.booleans.ImprovedBooleanCollection;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.booleans.BooleanComparator;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@Immutable
public abstract class ImmutableBooleanCollection implements ImprovedBooleanCollection {
    ImmutableBooleanCollection() {

    }

    public boolean isPartialView() {
        return false;
    }

    public abstract ImmutableBooleanList asList();

    public final ImmutableBooleanSet asSet() {
        return asSortedSet(null);
    }

    public abstract ImmutableBooleanSortedSet asSortedSet(@Nullable BooleanComparator comparator);

    @Override
    public abstract int hashCode();

    @Override
    public final ImmutableBooleanCollection clone() {
        return this;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        BooleanIterator it = iterator();
        StringBuilder sb = new StringBuilder(4 * size()).append('[').append(it.nextBoolean());

        while (it.hasNext()) {
            sb.append(", ").append(it.nextBoolean());
        }

        return sb.append(']').toString();
    }

    @Override
    public final Object @NotNull [] toArray() {
        return ImprovedBooleanCollection.super.toArray();
    }

    @Override
    public final <T> T @NotNull [] toArray(T @NotNull [] a) {
        return ImprovedBooleanCollection.super.toArray(a);
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public final boolean containsAll(@NotNull Collection<?> c) {
        return parallelBooleanStream().allMatch(c::contains);
    }

    @Override
    public final boolean addAll(@NotNull Collection<? extends Boolean> c) {
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
    public final boolean add(boolean key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(final boolean key) {
        return parallelBooleanStream().anyMatch(bl -> bl == key);
    }

    @Override
    public final boolean rem(boolean key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean[] toBooleanArray() {
        return toArray(BooleanArrays.DEFAULT_EMPTY_ARRAY);
    }

    @Override
    public boolean[] toArray(boolean[] a) {
        if (a.length < size()) {
            a = new boolean[size()];
        }

        BooleanIterator it = iterator();

        for (int i = 0; i < size(); i++) {
            a[i] = it.nextBoolean();
        }

        return a;
    }

    @Override
    public final boolean addAll(BooleanCollection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean containsAll(BooleanCollection c) {
        return parallelBooleanStream().allMatch(c::contains);
    }

    @Override
    public final boolean removeAll(BooleanCollection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean retainAll(BooleanCollection c) {
        throw new UnsupportedOperationException();
    }
}
