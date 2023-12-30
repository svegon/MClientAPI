package io.github.svegon.utils.fast.util.tuple.triplet;

import io.github.svegon.utils.fast.util.tuple.Triplet;

import java.util.Objects;

public abstract class AbstractTriplet<T, U, V> implements Triplet<T, U, V> {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Triplet<?, ?, ?> triplet)) {
            return false;
        }

        return Objects.equals(first(), triplet.first()) && Objects.equals(second(), triplet.second())
                && Objects.equals(third(), triplet.third());
    }

    @Override
    public int hashCode() {
        return 31 * (31 * Objects.hashCode(first()) + Objects.hashCode(second())) + Objects.hashCode(third());
    }

    @Override
    public String toString() {
        return "<" + first() + ", " + second() + ", " + third() + ">";
    }
}
