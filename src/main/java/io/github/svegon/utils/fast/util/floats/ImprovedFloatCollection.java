package io.github.svegon.utils.fast.util.floats;

import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.collections.stream.FloatStream;
import io.github.svegon.utils.collections.stream.StreamUtil;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @since 1.0.0
 */
public interface ImprovedFloatCollection extends FloatCollection {
    @Override
    default float[] toFloatArray() {
        return toArray((float[]) null);
    }

    @Override
    default float[] toArray(float[] a) {
        if (a == null) {
            a = new float[size()];
        } else if (a.length < size()) {
            a = Arrays.copyOf(a, size());
        }

        FloatIterator it = iterator();

        for (int i = 0; it.hasNext(); i++) {
            a[i] = it.nextFloat();
        }

        return a;
    }

    @Override
    default boolean addAll(FloatCollection c) {
        boolean modified = false;

        for (float f : c) {
            modified |= add(f);
        }

        return modified;
    }

    @Override
    default boolean containsAll(FloatCollection c) {
        return (c instanceof ImprovedFloatCollection ? ((ImprovedFloatCollection) c).parallelFloatStream()
                : StreamUtil.floatStream(c.spliterator(), true)).allMatch(this::contains);
    }

    /**
     * Return a fast stream over the elements, performing widening casts if
     * needed.
     *
     * @return a fast stream over the elements.
     * @see java.util.Collection#stream()
     * @see FloatStream
     */
    default FloatStream floatStream() {
        return StreamUtil.floatStream(spliterator(), false);
    }

    /**
     * Return a fast parallel stream over the elements, performing widening casts if
     * needed.
     *
     * @return a fast stream over the elements.
     * @see java.util.Collection#stream()
     * @see FloatStream
     */
    default FloatStream parallelFloatStream() {
        return StreamUtil.floatStream(spliterator(), true);
    }

    @Override
    default Object @NotNull [] toArray() {
        return toArray(ObjectArrays.DEFAULT_EMPTY_ARRAY);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default  <T> T @NotNull [] toArray(T @NotNull [] a) {
        FloatIterator it = iterator();
        int length = size();

        if (a.length < length) {
            a = ArrayUtil.newArray(a, size());
        }

        for (int i = 0; i < length; i++) {
            a[i] = (T) (Object) it.nextFloat();
        }

        return a;
    }
}
