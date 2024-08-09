package io.github.svegon.utils.fast.util.bytes;

import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import it.unimi.dsi.fastutil.bytes.ByteSpliterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public interface ByteMultiset extends Multiset<Byte>, ImprovedByteCollection {
    @Deprecated
    @Override
    default int count(Object o) {
        return o instanceof Byte ? count((byte) o) : 0;
    }

    int count(byte value);

    @Deprecated
    @Override
    default void forEach(@NotNull Consumer<? super Byte> action) {
        ImprovedByteCollection.super.forEach(action);
    }

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Deprecated
    @Override
    default int add(@Nullable Byte aByte, int i) {
        return add(aByte.byteValue(), i);
    }

    int add(byte value, int i);

    @Override
    default ByteSpliterator spliterator() {
        return ImprovedByteCollection.super.spliterator();
    }

    @Deprecated
    @Override
    default boolean add(Byte aByte) {
        return ImprovedByteCollection.super.add(aByte);
    }

    @Deprecated
    @Override
    default int remove(@Nullable Object o, int i) {
        return o instanceof Byte ? remove(((byte) o), i) : 0;
    }

    int remove(byte value, int i);

    @Deprecated
    @Override
    default boolean remove(@Nullable Object o) {
        return ImprovedByteCollection.super.remove(o);
    }

    @Override
    default boolean removeAll(final @NotNull ByteCollection c) {
        return removeIf(c::contains);
    }

    @Override
    default boolean retainAll(final @NotNull ByteCollection c) {
        return removeIf((s) -> !c.contains(s));
    }

    @Deprecated
    @Override
    default int setCount(@NotNull Byte aByte, int i) {
        return setCount(aByte.byteValue(), i);
    }

    int setCount(byte value, int i);

    @Override
    default boolean setCount(Byte aByte, int prev, int count) {
        return setCount(aByte.byteValue(), prev, count);
    }

    boolean setCount(byte value, int prev, int count);

    @Override
    ByteSet elementSet();

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    default Set<Multiset.Entry<Byte>> entrySet() {
        return (Set<Multiset.Entry<Byte>>) (Object) byteEntrySet();
    }

    Set<Entry> byteEntrySet();

    @Deprecated
    @Override
    default boolean contains(@Nullable Object o) {
        return ImprovedByteCollection.super.contains(o);
    }

    @Deprecated
    @Override
    boolean containsAll(final @NotNull Collection<?> collection);

    @Deprecated
    @Override
    boolean addAll(@NotNull Collection<? extends Byte> c);

    @Deprecated
    @Override
    default boolean removeAll(final @NotNull Collection<?> collection) {
        return collection instanceof ByteCollection ? removeAll((ByteCollection) collection)
                : removeIf(collection::contains);
    }

    @Deprecated
    @Override
    default boolean retainAll(final @NotNull Collection<?> collection) {
        return collection instanceof ByteCollection ? retainAll((ByteCollection) collection)
                : removeIf((bl) -> !collection.contains(bl));
    }

    interface Entry extends Multiset.Entry<Byte> {
        @Deprecated
        @Override
        default Byte getElement() {
            return getByteElement();
        }

        byte getByteElement();
    }
}
