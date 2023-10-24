package com.github.svegon.utils.fast.util.bytes.immutable;

import it.unimi.dsi.fastutil.bytes.ByteIterable;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Immutable
public abstract class ImmutableByteSet extends ImmutableByteCollection implements ByteSet {
    ImmutableByteSet(int hashCode) {
        super(hashCode);
    }

    @Override
    public final ImmutableByteSet toSet() {
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

        return s instanceof ByteSet ? containsAll((ByteSet) s) : containsAll(s);
    }

    @Override
    public Object @NotNull [] toArray() {
        return super.toArray();
    }

    @Override
    public <T> @NotNull T @NotNull [] toArray(T @NotNull [] a) {
        return super.toArray(a);
    }

    @Override
    public final boolean remove(byte k) {
        return rem(k);
    }

    public static ImmutableByteSet of(final byte @NotNull ... values) {
        return ImmutableByteSortedSet.of(null, values);
    }

    public static ImmutableByteSet copyOf(final @NotNull ByteIterable iterable) {
        return ImmutableByteSortedSet.copyOf(iterable, null);
    }

    public static ImmutableByteSet copyOf(final @NotNull ByteIterator iterator) {
        return ImmutableByteSortedSet.copyOf(iterator, null);
    }
}
