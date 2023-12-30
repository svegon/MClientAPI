package io.github.svegon.utils.fast.util.shorts;

import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import it.unimi.dsi.fastutil.shorts.ShortSpliterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public interface ShortMultiset extends Multiset<Short>, ImprovedShortCollection {
    @Deprecated
    @Override
    default int count(Object o) {
        return o instanceof Short ? count((short) o) : 0;
    }

    int count(short value);

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    default void forEach(@NotNull Consumer<? super Short> action) {
        ImprovedShortCollection.super.forEach(action);
    }

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Deprecated
    @Override
    default int add(@Nullable Short aShort, int i) {
        return aShort != null ? add(aShort.shortValue(), i) : 0;
    }

    int add(short value, int i);

    @Override
    default ShortSpliterator spliterator() {
        return ImprovedShortCollection.super.spliterator();
    }

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    default boolean add(Short aShort) {
        return ImprovedShortCollection.super.add(aShort);
    }

    @Deprecated
    @Override
    default int remove(@Nullable Object o, int i) {
        return o instanceof Short ? remove(((short) o), i) : 0;
    }

    int remove(short value, int i);

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    default boolean remove(@Nullable Object o) {
        return ImprovedShortCollection.super.remove(o);
    }

    @Override
    default boolean removeAll(final @NotNull ShortCollection c) {
        return removeIf(c::contains);
    }

    @Override
    default boolean retainAll(final @NotNull ShortCollection c) {
        return removeIf((s) -> !c.contains(s));
    }

    @Deprecated
    @Override
    default int setCount(@Nullable Short aShort, int i) {
        return aShort == null ? 0 : setCount(aShort.shortValue(), i);
    }

    int setCount(short value, int i);

    @Override
    default boolean setCount(Short aShort, int prev, int count) {
        return aShort != null && setCount(aShort.shortValue(), prev, count);
    }

    boolean setCount(short value, int prev, int count);

    @Override
    ShortSet elementSet();

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    default Set<Multiset.Entry<Short>> entrySet() {
        return (Set<Multiset.Entry<Short>>) (Object) shortEntrySet();
    }

    Set<Entry> shortEntrySet();

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    default boolean contains(@Nullable Object o) {
        return ImprovedShortCollection.super.contains(o);
    }

    @Deprecated
    @Override
    boolean containsAll(final @NotNull Collection<?> collection);

    @Deprecated
    @Override
    boolean addAll(@NotNull Collection<? extends Short> c);

    @Deprecated
    @Override
    default boolean removeAll(final @NotNull Collection<?> collection) {
        return collection instanceof ShortCollection ? removeAll((ShortCollection) collection)
                : removeIf(collection::contains);
    }

    @Deprecated
    @Override
    default boolean retainAll(final @NotNull Collection<?> collection) {
        return collection instanceof ShortCollection ? retainAll((ShortCollection) collection)
                : removeIf((bl) -> !collection.contains(bl));
    }

    interface Entry extends Multiset.Entry<Short> {
        @Deprecated
        @Override
        default Short getElement() {
            return getShortElement();
        }

        short getShortElement();
    }
}
