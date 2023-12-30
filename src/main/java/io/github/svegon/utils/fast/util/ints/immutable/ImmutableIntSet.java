package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.SetType;
import it.unimi.dsi.fastutil.ints.IntIterable;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Immutable
public abstract class ImmutableIntSet extends ImmutableIntCollection implements IntSet {
    ImmutableIntSet(int hashCode) {
        super(hashCode);
    }

    public abstract SetType getType();

    @Override
    public ImmutableIntList toList() {
        return new RegularImmutableIntList(toIntArray());
    }

    @Override
    public final ImmutableIntSet toSet() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Set<?> s)) {
            return false;
        }

        if (size() != s.size()) {
            return false;
        }

        return s instanceof IntSet ? containsAll((IntSet) s) : containsAll(s);
    }

    @Override
    public abstract boolean contains(int key);

    @Override
    public final boolean remove(int k) {
        return rem(k);
    }

    public static ImmutableIntSet of(final int @NotNull ... values) {
        return ImmutableIntSortedSet.of(null, values);
    }

    public static ImmutableIntSet copyOf(final @NotNull IntIterable iterable) {
        return ImmutableIntSortedSet.copyOf(iterable, null);
    }

    public static ImmutableIntSet copyOf(final @NotNull IntIterator iterator) {
        return ImmutableIntSortedSet.copyOf(iterator, null);
    }
}
