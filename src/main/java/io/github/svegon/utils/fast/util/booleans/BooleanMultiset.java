package io.github.svegon.utils.fast.util.booleans;

import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.booleans.*;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.bytes.ByteIterators;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public interface BooleanMultiset extends Multiset<Boolean>, BooleanCollection {
    @Deprecated
    @Override
    default void forEach(@NotNull Consumer<? super Boolean> action) {
        BooleanCollection.super.forEach(action);
    }

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Deprecated
    @Override
    default int count(Object o) {
        return o instanceof Boolean ? count((boolean) o) : 0;
    }

    int count(boolean e);

    @Deprecated
    @Override
    default int add(@Nullable Boolean aBoolean, int i) {
        return add(aBoolean != null && aBoolean, i);
    }

    int add(boolean bl, int i);

    @Override
    default @NotNull BooleanSpliterator spliterator() {
        return BooleanCollection.super.spliterator();
    }

    @Deprecated
    @Override
    default boolean add(Boolean aBoolean) {
        return BooleanCollection.super.add(aBoolean);
    }

    @Deprecated
    @Override
    default int remove(@Nullable Object o, int i) {
        return o instanceof Boolean ? remove(((boolean) o), i) : 0;
    }

    int remove(boolean bl, int i);

    @Deprecated
    @Override
    default boolean remove(@Nullable Object o) {
        return BooleanCollection.super.remove(o);
    }

    @Deprecated
    @Override
    default int setCount(Boolean aBoolean, int i) {
        return setCount(aBoolean != null && aBoolean, i);
    }

    int setCount(boolean aBoolean, int i);

    @Override
    default boolean setCount(Boolean aBoolean, int i, int i1) {
        return setCount(aBoolean != null && aBoolean, i, i1);
    }

    boolean setCount(boolean value, int i, int i1);

    @Override
    @NotNull
    BooleanSet elementSet();

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull Set<Multiset.Entry<Boolean>> entrySet() {
        return (Set<Multiset.Entry<Boolean>>) (Object) booleanEntrySet();
    }

    Set<Entry> booleanEntrySet();

    @Deprecated
    @Override
    default boolean contains(@Nullable Object o) {
        return BooleanCollection.super.contains(o);
    }

    @Deprecated
    @Override
    boolean containsAll(final @NotNull Collection<?> collection);

    @Deprecated
    @Override
    boolean addAll(@NotNull Collection<? extends Boolean> c);

    @Deprecated
    @Override
    default boolean removeAll(final @NotNull Collection<?> collection) {
        return collection instanceof BooleanCollection ? removeAll((BooleanCollection) collection)
                : removeIf(collection::contains);
    }

    @Deprecated
    @Override
    default boolean retainAll(final @NotNull Collection<?> collection) {
        return collection instanceof BooleanCollection ? retainAll((BooleanCollection) collection)
                : removeIf((bl) -> !collection.contains(bl));
    }

    @Override
    default boolean[] toBooleanArray() {
        return toArray(BooleanArrays.DEFAULT_EMPTY_ARRAY);
    }

    @Override
    default boolean[] toArray(boolean[] a) {
        if (a == null || a.length < size()) {
            return BooleanIterators.unwrap(iterator());
        }

        var i = iterator();
        var unwrapped = 0;

        while ((unwrapped += BooleanIterators.unwrap(i, a)) < size()) {
            a = BooleanArrays.ensureCapacity(a, size());
        }

        return a;
    }

    @Override
    default boolean containsAll(BooleanCollection c) {
        return c.parallelStream().allMatch(this::contains);
    }

    @Override
    default boolean removeAll(BooleanCollection c) {
        return removeIf(c::contains);
    }

    @Override
    default boolean retainAll(BooleanCollection c) {
        return removeIf(bl -> !c.contains(bl));
    }

    interface Entry extends Multiset.Entry<Boolean> {
        @Deprecated
        @Override
        default Boolean getElement() {
            return getBooleanElement();
        }

        boolean getBooleanElement();
    }
}
