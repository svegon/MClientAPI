package io.github.svegon.utils.fast.util.longs;

import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSpliterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public interface LongMultiset extends Multiset<Long>, LongCollection {
    @Deprecated
    @Override
    default int count(Object o) {
        return o instanceof Long ? count((long) o) : 0;
    }

    int count(long value);

    @Deprecated
    @Override
    default void forEach(@NotNull Consumer<? super Long> action) {
        LongCollection.super.forEach(action);
    }

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Deprecated
    @Override
    default int add(@Nullable Long aLong, int i) {
        return add(aLong.longValue(), i);
    }

    int add(long value, int i);

    @Override
    default LongSpliterator spliterator() {
        return LongCollection.super.spliterator();
    }

    @Deprecated
    @Override
    default boolean add(Long aLong) {
        return LongCollection.super.add(aLong);
    }

    @Deprecated
    @Override
    default int remove(@Nullable Object o, int i) {
        return o instanceof Long ? remove(((long) o), i) : 0;
    }

    int remove(long value, int i);

    @Deprecated
    @Override
    default boolean remove(@Nullable Object o) {
        return LongCollection.super.remove(o);
    }

    @Override
    default boolean removeAll(final @NotNull LongCollection c) {
        return removeIf(c::contains);
    }

    @Override
    default boolean retainAll(final @NotNull LongCollection c) {
        return removeIf((s) -> !c.contains(s));
    }

    @Deprecated
    @Override
    default int setCount(@NotNull Long aLong, int i) {
        return setCount(aLong.longValue(), i);
    }

    int setCount(long value, int i);

    @Override
    default boolean setCount(Long aLong, int prev, int count) {
        return setCount(aLong.longValue(), prev, count);
    }

    boolean setCount(long value, int prev, int count);

    @Override
    LongSet elementSet();

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    default Set<Multiset.Entry<Long>> entrySet() {
        return (Set<Multiset.Entry<Long>>) (Object) longEntrySet();
    }

    Set<Entry> longEntrySet();

    @Deprecated
    @Override
    default boolean contains(@Nullable Object o) {
        return LongCollection.super.contains(o);
    }

    @Deprecated
    @Override
    boolean containsAll(final @NotNull Collection<?> collection);

    @Deprecated
    @Override
    boolean addAll(@NotNull Collection<? extends Long> c);

    @Deprecated
    @Override
    default boolean removeAll(final @NotNull Collection<?> collection) {
        return collection instanceof LongCollection ? removeAll((LongCollection) collection)
                : removeIf(collection::contains);
    }

    @Deprecated
    @Override
    default boolean retainAll(final @NotNull Collection<?> collection) {
        return collection instanceof LongCollection ? retainAll((LongCollection) collection)
                : removeIf((bl) -> !collection.contains(bl));
    }

    interface Entry extends Multiset.Entry<Long> {
        @Deprecated
        @Override
        default Long getElement() {
            return getLongElement();
        }

        long getLongElement();
    }
}
