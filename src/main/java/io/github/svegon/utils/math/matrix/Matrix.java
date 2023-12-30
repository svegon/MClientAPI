package io.github.svegon.utils.math.matrix;

import io.github.svegon.utils.annotations.TrustedMutableArg;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * By default matrices are mutable.
 *
 *
 * All dimensions of a matrix must be positive. That means
 *
 * @param <A> an array type representing an array of elements of this matrix
 * @param <L> type for the primitive for this matrix
 */
public interface Matrix<A, L extends List<?>> {
    int width();

    int height();

    @NotNull A asArray();

    L getColumn(final int index);

    L getRow(final int index);

    default void set(final @NotNull A values) {
        throw new UnsupportedOperationException();
    }

    default void transpose() {
        throw new UnsupportedOperationException();
    }

    @NotNull Matrix<A, L> transposed();

    /**
     * in state multiplication of each element by the corresponding element in {@code m}
     * @param m
     */
    default void mul(final @NotNull Matrix<A, L> m) {
        throw new UnsupportedOperationException();
    }
    
    @NotNull Matrix<A, L> multiply(final @NotNull Matrix<A, L> m);

    /**
     * simulates a larger square with sides of the size
     *
     * @param vector
     * @return {@param this} * {@param vector} as a matrix multiplication
     */
    @Contract("_ -> param1")
    A multiply(final @NotNull @TrustedMutableArg(isMutated = true) A vector);

    @Override
    boolean equals(@Nullable Object o);

    /**
     * @return (Integer.hashCode(width()) * 961 + Integer.hashCode(height()) * 31 + Arrays.hashCode(asArray())
     */
    @Override
    int hashCode();

    Matrix<A, L> clone();

    @Override
    @NotNull String toString();
}
