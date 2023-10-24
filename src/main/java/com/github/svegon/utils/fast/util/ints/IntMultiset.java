package com.github.svegon.utils.fast.util.ints;

import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.ints.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Set;
import java.util.function.Consumer;

public interface IntMultiset extends Multiset<Integer>, IntCollection {
    @Override
    default int[] toIntArray() {
        return toArray(IntArrays.DEFAULT_EMPTY_ARRAY);
    }

    @Override
    default int[] toArray(int[] a) {
        if (a.length < size()) {
            a = Arrays.copyOf(a, size());
        }

        int i = 0;

        try {
            for (IntIterator it = iterator(); it.hasNext(); i++) {
                a[i] = it.nextInt();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ConcurrentModificationException();
        }

        return a;
    }

    @Override
    default boolean addAll(IntCollection c) {
        boolean modified = false;

        for (int i : c) {
            modified |= add(i);
        }

        return modified;
    }

    @Override
    default boolean containsAll(IntCollection c) {
        for (int i : c) {
            if (!contains(i)) {
                return false;
            }
        }

        return true;
    }

    @Deprecated
    @Override
    default int count(Object o) {
        return o instanceof Integer ? count((int) o) : 0;
    }

    int count(int value);

    @Deprecated
    @Override
    default void forEach(@NotNull Consumer<? super Integer> action) {
        IntCollection.super.forEach(action);
    }

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Deprecated
    @Override
    default int add(@Nullable Integer aInt, int i) {
        return add(aInt.intValue(), i);
    }

    int add(int value, int i);

    @Override
    default IntSpliterator spliterator() {
        return IntCollection.super.spliterator();
    }

    @Deprecated
    @Override
    default boolean add(Integer aInt) {
        return IntCollection.super.add(aInt);
    }

    @Deprecated
    @Override
    default int remove(@Nullable Object o, int i) {
        return o instanceof Integer ? remove(((int) o), i) : 0;
    }

    int remove(int value, int i);

    @Deprecated
    @Override
    default boolean remove(@Nullable Object o) {
        return IntCollection.super.remove(o);
    }

    @Override
    default boolean removeAll(final @NotNull IntCollection c) {
        return removeIf(c::contains);
    }

    @Override
    default boolean retainAll(final @NotNull IntCollection c) {
        return removeIf((s) -> !c.contains(s));
    }

    @Deprecated
    @Override
    default int setCount(@NotNull Integer aInt, int i) {
        return setCount(aInt.intValue(), i);
    }

    int setCount(int value, int i);

    @Override
    default boolean setCount(Integer aInt, int prev, int count) {
        return setCount(aInt.intValue(), prev, count);
    }

    boolean setCount(int value, int prev, int count);

    @Override
    IntSet elementSet();

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    default Set<Multiset.Entry<Integer>> entrySet() {
        return (Set<Multiset.Entry<Integer>>) (Object) intEntrySet();
    }

    Set<Entry> intEntrySet();

    @Deprecated
    @Override
    default boolean contains(@Nullable Object o) {
        return IntCollection.super.contains(o);
    }

    @Deprecated
    @Override
    boolean containsAll(final @NotNull Collection<?> collection);

    @Deprecated
    @Override
    boolean addAll(@NotNull Collection<? extends Integer> c);

    @Deprecated
    @Override
    default boolean removeAll(final @NotNull Collection<?> collection) {
        return collection instanceof IntCollection ? removeAll((IntCollection) collection)
                : removeIf(collection::contains);
    }

    @Deprecated
    @Override
    default boolean retainAll(final @NotNull Collection<?> collection) {
        return collection instanceof IntCollection ? retainAll((IntCollection) collection)
                : removeIf((bl) -> !collection.contains(bl));
    }

    interface Entry extends Multiset.Entry<Integer> {
        @Deprecated
        @Override
        default Integer getElement() {
            return getIntElement();
        }

        int getIntElement();
    }
}
