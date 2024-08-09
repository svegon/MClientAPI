package io.github.svegon.utils.fast.util.floats;

import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.floats.FloatSet;
import it.unimi.dsi.fastutil.floats.FloatSpliterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public interface FloatMultiset extends Multiset<Float>, ImprovedFloatCollection {
    @Deprecated
    @Override
    default int count(Object o) {
        return o instanceof Float ? count((float) o) : 0;
    }

    int count(float value);

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    default void forEach(@NotNull Consumer<? super Float> action) {
        ImprovedFloatCollection.super.forEach(action);
    }

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Deprecated
    @Override
    default int add(@Nullable Float aFloat, int i) {
        return aFloat != null ? add(aFloat.floatValue(), i) : 0;
    }

    int add(float value, int i);

    @Override
    default FloatSpliterator spliterator() {
        return ImprovedFloatCollection.super.spliterator();
    }

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    default boolean add(Float aFloat) {
        return ImprovedFloatCollection.super.add(aFloat);
    }

    @Deprecated
    @Override
    default int remove(@Nullable Object o, int i) {
        return o instanceof Float ? remove(((float) o), i) : 0;
    }

    int remove(float value, int i);

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    default boolean remove(@Nullable Object o) {
        return ImprovedFloatCollection.super.remove(o);
    }

    @Override
    default boolean removeAll(final @NotNull FloatCollection c) {
        return removeIf(c::contains);
    }

    @Override
    default boolean retainAll(final @NotNull FloatCollection c) {
        return removeIf((s) -> !c.contains(s));
    }

    @Deprecated
    @Override
    default int setCount(@Nullable Float aFloat, int i) {
        return aFloat == null ? 0 : setCount(aFloat.floatValue(), i);
    }

    int setCount(float value, int i);

    @Override
    default boolean setCount(Float aFloat, int prev, int count) {
        return aFloat != null && setCount(aFloat.floatValue(), prev, count);
    }

    boolean setCount(float value, int prev, int count);

    @Override
    FloatSet elementSet();

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    default Set<Multiset.Entry<Float>> entrySet() {
        return (Set<Multiset.Entry<Float>>) (Object) floatEntrySet();
    }

    Set<Entry> floatEntrySet();

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    default boolean contains(@Nullable Object o) {
        return ImprovedFloatCollection.super.contains(o);
    }

    @Deprecated
    @Override
    boolean containsAll(final @NotNull Collection<?> collection);

    @Deprecated
    @Override
    boolean addAll(@NotNull Collection<? extends Float> c);

    @Deprecated
    @Override
    default boolean removeAll(final @NotNull Collection<?> collection) {
        return collection instanceof FloatCollection ? removeAll((FloatCollection) collection)
                : removeIf(collection::contains);
    }

    @Deprecated
    @Override
    default boolean retainAll(final @NotNull Collection<?> collection) {
        return collection instanceof FloatCollection ? retainAll((FloatCollection) collection)
                : removeIf((bl) -> !collection.contains(bl));
    }

    interface Entry extends Multiset.Entry<Float> {
        @Deprecated
        @Override
        default Float getElement() {
            return getFloatElement();
        }

        float getFloatElement();
    }
}
