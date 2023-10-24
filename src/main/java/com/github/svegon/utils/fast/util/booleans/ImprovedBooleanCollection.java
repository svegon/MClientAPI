package com.github.svegon.utils.fast.util.booleans;

import com.github.svegon.utils.collections.ArrayUtil;
import com.github.svegon.utils.collections.stream.BooleanStream;
import com.github.svegon.utils.collections.stream.StreamUtil;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import org.jetbrains.annotations.NotNull;

public interface ImprovedBooleanCollection extends BooleanCollection {
    @Override
    default boolean[] toBooleanArray() {
        return toArray((boolean[]) null);
    }

    @Override
    default boolean[] toArray(boolean[] a) {
        if (a == null || a.length < size()) {
            a = new boolean[size()];
        }

        BooleanIterator it = iterator();

        for (int i = 0; it.hasNext(); i++) {
            a[i] = it.nextBoolean();
        }

        return a;
    }

    @Override
    default boolean addAll(BooleanCollection c) {
        boolean modified = false;

        for (boolean bl : c) {
            modified |= add(bl);
        }

        return modified;
    }

    @Override
    default boolean containsAll(BooleanCollection c) {
        return (c instanceof ImprovedBooleanCollection ? ((ImprovedBooleanCollection) c).parallelBooleanStream()
                : StreamUtil.booleanStream(c.spliterator(), true)).allMatch(this::contains);
    }

    @Override
    default boolean removeAll(final @NotNull BooleanCollection c) {
        return removeIf(c::contains);
    }

    @Override
    default boolean retainAll(final @NotNull BooleanCollection c) {
        return removeIf((bl) -> !c.contains(bl));
    }

    /**
    * Return a fast stream over the elements, performing widening casts if
    * needed.
    *
    * @return a fast stream over the elements.
    * @see java.util.Collection#stream()
    * @see BooleanStream
    */
    default BooleanStream booleanStream() {
        return StreamUtil.booleanStream(spliterator(), false);
    }

    /**
     * Return a fast parallel stream over the elements, performing widening casts if
     * needed.
     *
     * @return a fast stream over the elements.
     * @see java.util.Collection#stream()
     * @see BooleanStream
     */
    default BooleanStream parallelBooleanStream() {
        return StreamUtil.booleanStream(spliterator(), true);
    }

    @Override
    default Object @NotNull [] toArray() {
        return toArray(ObjectArrays.DEFAULT_EMPTY_ARRAY);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default  <T> T @NotNull [] toArray(T @NotNull [] a) {
        int length = size();
        BooleanIterator it = iterator();

        if (a.length < length) {
            a = ArrayUtil.newArray(a, size());
        }

        for (int i = 0; i < length; i++) {
            a[i] = (T) (Object) it.nextBoolean();
        }

        return a;
    }
}
