package io.github.svegon.utils.fast.util.doubles;

import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.doubles.DoubleSet;
import it.unimi.dsi.fastutil.doubles.DoubleSpliterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public interface DoubleMultiset extends Multiset<Double>, DoubleCollection {
    @Deprecated
    @Override
    default int count(Object o) {
        return o instanceof Double ? count((double) o) : 0;
    }

    int count(double value);

    @Deprecated
    @Override
    default void forEach(@NotNull Consumer<? super Double> action) {
        DoubleCollection.super.forEach(action);
    }

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Deprecated
    @Override
    default int add(@Nullable Double aDouble, int i) {
        return add(aDouble.doubleValue(), i);
    }

    int add(double value, int i);

    @Override
    default DoubleSpliterator spliterator() {
        return DoubleCollection.super.spliterator();
    }

    @Deprecated
    @Override
    default boolean add(Double aDouble) {
        return DoubleCollection.super.add(aDouble);
    }

    @Deprecated
    @Override
    default int remove(@Nullable Object o, int i) {
        return o instanceof Double ? remove(((double) o), i) : 0;
    }

    int remove(double value, int i);

    @Deprecated
    @Override
    default boolean remove(@Nullable Object o) {
        return DoubleCollection.super.remove(o);
    }

    @Override
    default boolean removeAll(final @NotNull DoubleCollection c) {
        return removeIf(c::contains);
    }

    @Override
    default boolean retainAll(final @NotNull DoubleCollection c) {
        return removeIf((s) -> !c.contains(s));
    }

    @Deprecated
    @Override
    default int setCount(@NotNull Double aDouble, int i) {
        return setCount(aDouble.doubleValue(), i);
    }

    int setCount(double value, int i);

    @Override
    default boolean setCount(Double aDouble, int prev, int count) {
        return setCount(aDouble.doubleValue(), prev, count);
    }

    boolean setCount(double value, int prev, int count);

    @Override
    DoubleSet elementSet();

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    default Set<Multiset.Entry<Double>> entrySet() {
        return (Set<Multiset.Entry<Double>>) (Object) doubleEntrySet();
    }

    Set<Entry> doubleEntrySet();

    @Deprecated
    @Override
    default boolean contains(@Nullable Object o) {
        return DoubleCollection.super.contains(o);
    }

    @Deprecated
    @Override
    boolean containsAll(final @NotNull Collection<?> collection);

    @Deprecated
    @Override
    boolean addAll(@NotNull Collection<? extends Double> c);

    @Deprecated
    @Override
    default boolean removeAll(final @NotNull Collection<?> collection) {
        return collection instanceof DoubleCollection ? removeAll((DoubleCollection) collection)
                : removeIf(collection::contains);
    }

    @Deprecated
    @Override
    default boolean retainAll(final @NotNull Collection<?> collection) {
        return collection instanceof DoubleCollection ? retainAll((DoubleCollection) collection)
                : removeIf((bl) -> !collection.contains(bl));
    }

    interface Entry extends Multiset.Entry<Double> {
        @Deprecated
        @Override
        default Double getElement() {
            return getDoubleElement();
        }

        double getDoubleElement();
    }
}
