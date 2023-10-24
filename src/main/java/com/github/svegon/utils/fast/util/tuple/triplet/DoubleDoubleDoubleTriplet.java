package com.github.svegon.utils.fast.util.tuple.triplet;

import com.github.svegon.utils.fast.util.tuple.Triplet;
import com.github.svegon.utils.math.geometry.vector.Vec3d;
import org.jetbrains.annotations.Contract;

public interface DoubleDoubleDoubleTriplet extends Triplet<Double, Double, Double> {
    @Deprecated
    default Double first() {
        return thirdDouble();
    }

    @Deprecated
    default Double second() {
        return thirdDouble();
    }

    @Deprecated
    default Double third() {
        return thirdDouble();
    }

    @Deprecated
    @Contract("_ -> this")
    default DoubleDoubleDoubleTriplet first(final Double first) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Contract("_ -> this")
    default DoubleDoubleDoubleTriplet second(final Double second) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Contract("_ -> this")
    default DoubleDoubleDoubleTriplet third(final Double third) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the first element of the triplet.
     *
     * @return the first element of the triplet
     */
    double firstDouble();

    /**
     * Returns the second element of the triplet.
     *
     * @return the second element of the triplet
     */
    double secondDouble();

    /**
     * Returns the third element of the triplet.
     *
     * @return the third element of the triplet
     */
    double thirdDouble();

    /**
     * Sets the first element of this triplet (optional operation).
     *
     * @param first a new value for the left element.
     */
    @Contract("_ -> this")
    default DoubleDoubleDoubleTriplet first(final double first) {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets the second element of this triplet (optional operation).
     *
     * @param second a new value for the left element.
     */
    @Contract("_ -> this")
    default DoubleDoubleDoubleTriplet second(final double second) {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets the third element of this triplet (optional operation).
     *
     * @param third a new value for the left element.
     */
    @Contract("_ -> this")
    default DoubleDoubleDoubleTriplet third(final double third) {
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
    static Vec3d of(double first, double second, double third) {
        return new Vec3d(first, second, third);
    }
}
