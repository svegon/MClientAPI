package com.github.svegon.utils.fast.util.tuple;

import com.github.svegon.utils.fast.util.tuple.triplet.ObjectObjectObjectImmutableTriplet;
import org.jetbrains.annotations.Contract;

public interface Triplet<T, U, V> {
    /**
     * Returns the first element of the triplet.
     *
     * @return the first element of the triplet
     */
    T first();

    /**
     * Returns the second element of the triplet.
     *
     * @return the second element of the triplet
     */
    U second();

    /**
     * Returns the third element of the triplet.
     *
     * @return the third element of the triplet
     */
    V third();

    /**
     * Sets the first element of this triplet (optional operation).
     *
     * @param first a new value for the left element.
     */
    @Contract("_ -> this")
    default Triplet<T, U, V> first(final T first) {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets the second element of this triplet (optional operation).
     *
     * @param second a new value for the left element.
     */
    @Contract("_ -> this")
    default Triplet<T, U, V> second(final U second) {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets the third element of this triplet (optional operation).
     *
     * @param third a new value for the left element.
     */
    @Contract("_ -> this")
    default Triplet<T, U, V> third(final V third) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a new immutable {@link Triplet Pair} with given first, second and third values.
     *
     * @param first the first value
     * @param second the second value
     * @param third the third value
     *
     * @implNote This factory method returns an instance of {@link ObjectObjectObjectImmutableTriplet}.
     */
    static <T, U, V> Triplet<T, U, V> of(T first, U second, V third) {
        return new ObjectObjectObjectImmutableTriplet<>(first, second, third);
    }
}
