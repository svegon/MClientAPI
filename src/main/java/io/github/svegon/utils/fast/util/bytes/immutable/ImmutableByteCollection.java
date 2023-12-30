package io.github.svegon.utils.fast.util.bytes.immutable;

import io.github.svegon.utils.fast.util.bytes.ImprovedByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteComparator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@Immutable
public abstract class ImmutableByteCollection implements ImprovedByteCollection {
    private final int hashCode;

    ImmutableByteCollection(int hashCode) {
        this.hashCode = hashCode;
    }

    public boolean isPartialView() {
        return false;
    }

    public ImmutableByteList toList() {
        return new RegularImmutableByteList(toByteArray());
    }

    public ImmutableByteSet toSet() {
        return toSortedSet(null);
    }

    public abstract ImmutableByteSortedSet toSortedSet(@Nullable ByteComparator comparator);

    @Override
    public abstract boolean equals(Object o);

    @Override
    public final int hashCode() {
        return hashCode;
    }

    @Override
    public final ImmutableByteCollection clone() throws CloneNotSupportedException {
        return this;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        ByteIterator it = iterator();
        StringBuilder sb = new StringBuilder(4 * size()).append('[').append(it.nextByte());

        while (it.hasNext()) {
            sb.append(", ").append(it.nextByte());
        }

        return sb.append(']').toString();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public final boolean containsAll(@NotNull Collection<?> c) {
        return parallelByteStream().allMatch(c::contains);
    }

    @Override
    public final boolean addAll(@NotNull Collection<? extends Byte> c) {
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
    public final boolean add(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(final byte key) {
        return parallelByteStream().anyMatch(bl -> bl == key);
    }

    @Override
    public final boolean rem(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final byte[] toByteArray() {
        return toArray(ByteArrays.EMPTY_ARRAY);
    }

    @Override
    public byte[] toArray(byte[] a) {
        if (a.length < size()) {
            a = new byte[size()];
        }

        ByteIterator it = iterator();

        for (int i = 0; i < size(); i++) {
            a[i] = it.nextByte();
        }

        return a;
    }

    @Override
    public final boolean addAll(ByteCollection c) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean containsAll(ByteCollection c) {
        return parallelByteStream().allMatch(c::contains);
    }

    @Override
    public final boolean removeAll(ByteCollection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean retainAll(ByteCollection c) {
        throw new UnsupportedOperationException();
    }
}
