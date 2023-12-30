package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.ArrayUtil;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;

@Immutable
public abstract class ImmutableIntCollection implements IntCollection {
    private final int hashCode;

    ImmutableIntCollection(int hashCode) {
        this.hashCode = hashCode;
    }

    public abstract ImmutableIntList toList();

    public ImmutableIntSet toSet() {
        return toSortedSet(null);
    }

    public abstract ImmutableIntSortedSet toSortedSet(final @Nullable IntComparator comparator);

    @Override
    public final int hashCode() {
        return hashCode;
    }

    @Override
    public ImmutableIntCollection clone() {
        return this;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        IntIterator it = iterator();
        StringBuilder sb = new StringBuilder(4 * size()).append('[').append(it.nextInt());

        while (it.hasNext()) {
            sb.append(", ").append(it.nextInt());
        }

        return sb.append(']').toString();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public final Object @NotNull [] toArray() {
        return toArray(ObjectArrays.DEFAULT_EMPTY_ARRAY);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public final  <T> T @NotNull [] toArray(T @NotNull [] a) {
        if (a.length < size()) {
            a = ArrayUtil.newArray(a, size());
        }

        IntIterator it = iterator();
        int length = size();

        for (int i = 0; i < length; i++) {
            a[i] = (T) (Object) it.nextInt();
        }

        return a;
    }

    @Override
    public final boolean containsAll(@NotNull Collection<?> c) {
        if (c instanceof IntCollection)
            return containsAll((IntCollection) c);

        for (Object o : c) {
            if (!(o instanceof Integer) || !contains((int) o)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public final boolean addAll(@NotNull Collection<? extends Integer> c) {
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
    public final boolean add(int key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(final int key) {
        return intParallelStream().anyMatch(bl -> bl == key);
    }

    @Override
    public final boolean rem(int key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int[] toIntArray() {
        return toArray((int[]) null);
    }

    @Override
    public int[] toArray(int @Nullable [] a) {
        if (a == null) {
            a = new int[size()];
        } else if (a.length < size()) {
            a = Arrays.copyOf(a, size());
        }

        IntIterator it = iterator();

        for (int i = 0; i < size(); i++) {
            a[i] = it.nextInt();
        }

        return a;
    }

    @Override
    public final boolean addAll(IntCollection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean containsAll(IntCollection c) {
        return c.intParallelStream().allMatch(this::contains);
    }

    @Override
    public final boolean removeAll(IntCollection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean retainAll(IntCollection c) {
        throw new UnsupportedOperationException();
    }
}
