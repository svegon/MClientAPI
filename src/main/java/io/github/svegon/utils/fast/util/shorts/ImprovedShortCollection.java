package io.github.svegon.utils.fast.util.shorts;

import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.collections.stream.ShortStream;
import io.github.svegon.utils.collections.stream.StreamUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import it.unimi.dsi.fastutil.shorts.ShortArrays;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.ConcurrentModificationException;

/**
 * @since 1.0.0
 */
public interface ImprovedShortCollection extends ShortCollection {
    @Override
    default short[] toShortArray() {
        return toArray(ShortArrays.DEFAULT_EMPTY_ARRAY);
    }

    @Override
    default short[] toArray(short[] a) {
        if (a == null) {
            a = new short[size()];
        } else if (a.length < size()) {
            a = Arrays.copyOf(a, size());
        }

        ShortIterator it = iterator();

        try {
            for (int i = 0; it.hasNext(); i++) {
                a[i] = it.nextShort();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ConcurrentModificationException();
        }

        return a;
    }

    @Override
    default boolean removeAll(final @NotNull ShortCollection c) {
        boolean modified = false;

        for (short s : c) {
            modified |= this.rem(s);
        }

        return modified;
    }

    @Override
    default boolean retainAll(final @NotNull ShortCollection c) {
        return removeIf((e) -> !c.contains(e));
    }

    @Override
    default boolean addAll(ShortCollection c) {
        boolean modified = false;

        for (short s : c) {
            modified |= add(s);
        }

        return modified;
    }

    @Override
    default boolean containsAll(ShortCollection c) {
        for (short s : c) {
            if (!contains(s)) {
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
     * @see ShortStream
     */
    default ShortStream shortStream() {
        return StreamUtil.shortStream(spliterator(), false);
    }

    /**
     * Return a fast parallel stream over the elements, performing widening casts if
     * needed.
     *
     * @return a fast stream over the elements.
     * @see java.util.Collection#stream()
     * @see ShortStream
     */
    default ShortStream parallelShortStream() {
        return StreamUtil.shortStream(spliterator(), true);
    }

    @Override
    default Object @NotNull [] toArray() {
        return toArray(ObjectArrays.DEFAULT_EMPTY_ARRAY);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default  <T> T @NotNull [] toArray(T @NotNull [] a) {
        ShortIterator it = iterator();
        int length = size();

        if (a.length < length) {
            a = ArrayUtil.newArray(a, size());
        }

        for (int i = 0; i < length; i++) {
            a[i] = (T) it.next();
        }

        return a;
    }
}
