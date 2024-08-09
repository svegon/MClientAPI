package io.github.svegon.utils.fast.util.booleans.immutable;

import it.unimi.dsi.fastutil.booleans.*;
import org.jetbrains.annotations.NotNull;

import java.util.RandomAccess;

public abstract class ImmutableBooleanSet extends ImmutableBooleanCollection implements BooleanSet, RandomAccess {
    @Override
    public ImmutableBooleanList asList() {
        return new RegularImmutableBooleanList(toBooleanArray());
    }

    @Override
    public final boolean contains(Object key) {
        return key instanceof Boolean && contains((boolean) key);
    }

    @Deprecated
    @Override
    public final boolean add(Boolean k) {
        return super.add(k);
    }

    @Deprecated
    @Override
    public final boolean remove(Object key) {
        return key instanceof Boolean && remove((boolean) key);
    }

    @Override
    public final boolean remove(boolean k) {
        throw new UnsupportedOperationException();
    }

    public static ImmutableBooleanSortedSet of(final boolean @NotNull ... values) {
        return ImmutableBooleanSortedSet.of(null, values);
    }

    public static ImmutableBooleanSortedSet copyOf(final @NotNull BooleanIterable iterable) {
        return ImmutableBooleanSortedSet.copyOf(iterable, null);
    }

    public static ImmutableBooleanSortedSet copyOf(final @NotNull BooleanIterator iterator) {
        return ImmutableBooleanSortedSet.copyOf(null, iterator);
    }
}
