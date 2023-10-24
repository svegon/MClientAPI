package com.github.svegon.utils.fast.util.bytes;

import com.github.svegon.utils.collections.ArrayUtil;
import com.github.svegon.utils.collections.stream.ByteStream;
import com.github.svegon.utils.collections.stream.StreamUtil;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import org.jetbrains.annotations.NotNull;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * @since 1.0.0
 */
public interface ImprovedByteCollection extends ByteCollection {
    @Override
    default byte[] toByteArray() {
        return toArray((byte[]) null);
    }

    @Override
    default byte[] toArray(byte[] a) {
        if (a == null || a.length < size()) {
            a = new byte[size()];
        }

        ByteIterator it = iterator();

        try {
            for (int i = 0; it.hasNext(); i++) {
                a[i] = it.nextByte();
            }
        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            throw new ConcurrentModificationException(e);
        }

        return a;
    }

    @Override
    default boolean addAll(ByteCollection c) {
        boolean modified = false;

        for (byte b : c) {
            modified |= add(b);
        }

        return modified;
    }

    @Override
    default boolean containsAll(ByteCollection c) {
        for (byte b : c) {
            if (!c.contains(b)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Return a fast stream over the elements, performing widening casts if
     * needed.
     *
     * @return a fast stream over the elements.
     * @see java.util.Collection#stream()
     * @see ByteStream
     */
    default ByteStream byteStream() {
        return StreamUtil.byteStream(spliterator(), false);
    }

    /**
     * Return a fast parallel stream over the elements, performing widening casts if
     * needed.
     *
     * @return a fast stream over the elements.
     * @see java.util.Collection#stream()
     * @see ByteStream
     */
    default ByteStream parallelByteStream() {
        return StreamUtil.byteStream(spliterator(), true);
    }

    @Override
    default Object @NotNull [] toArray() {
        return toArray(ObjectArrays.DEFAULT_EMPTY_ARRAY);
    }

    @Override
    @SuppressWarnings("unchecked")
    default <T> T @NotNull [] toArray(T @NotNull [] a) {
        ByteIterator it = iterator();
        int length = size();

        if (a.length < length) {
            a = ArrayUtil.newArray(a, length);
        }

        for (int i = 0; i < length; i++) {
            a[i] = (T) (Object) it.nextByte();
        }

        return a;
    }
}
