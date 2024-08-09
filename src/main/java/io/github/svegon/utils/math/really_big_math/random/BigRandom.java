package io.github.svegon.utils.math.really_big_math.random;

import io.github.svegon.utils.collections.ArrayUtil;
import com.google.common.base.Preconditions;
import io.github.svegon.utils.collections.stream.*;
import it.unimi.dsi.fastutil.Swapper;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.chars.CharArrays;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.floats.FloatArrays;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.longs.LongArrays;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import it.unimi.dsi.fastutil.shorts.ShortArrays;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.random.RandomGenerator;

/**
 * a RandomGenerator which uses large seeds represented by int arrays
 * The main purpose of BigRandom classes is generating seed-based data
 * from larger seeds than those used by {@link Random}.
 */
public interface BigRandom extends RandomGenerator {
    int[] getSeed();

    void setSeed(int[] seed);

    default boolean nextBoolean(double chance) {
        return nextDouble() < chance;
    }

    default boolean nextBoolean(int inverseChance) {
        return nextInt(inverseChance) == 0;
    }

    default byte nextByte() {
        return (byte) (nextInt() >>> 24);
    }

    default byte nextByte(byte bound) {
        return (byte) nextInt(bound);
    }

    default byte nextByte(byte origin, byte bound) {
        return (byte) nextInt(origin, bound);
    }

    default char nextChar() {
        return (char) (nextInt() >> 16);
    }

    default char nextChar(char bound) {
        return (char) nextInt(bound);
    }

    default char nextChar(char origin, char bound) {
        return (char) nextInt(origin, bound);
    }

    default short nextShort() {
        return (short) (nextInt() >> 16);
    }

    default short nextShort(short bound) {
        return (short) nextInt(bound);
    }

    default short nextShort(short origin, short bound) {
        return (short) nextInt(origin, bound);
    }

    @Override
    int nextInt();

    @Override
    long nextLong();

    @Override
    default float nextFloat() {
        return (nextInt() >>> 8) / ((float)(1 << 24));
    }

    @Override
    default double nextDouble() {
        return (((long)(nextInt() >>> 6) << 27) + (nextInt() >>> 5)) * 0x1.0p-53;
    }

    default boolean[] nextBooleanArray(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return BooleanArrays.EMPTY_ARRAY;
        } else {
            return nextBooleanArray(new boolean[size]);
        }
    }

    boolean[] nextBooleanArray(final boolean @NotNull [] a);

    default byte[] nextByteArray(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return ByteArrays.EMPTY_ARRAY;
        } else {
            return nextByteArray(new byte[size]);
        }
    }

    byte[] nextByteArray(final byte @NotNull [] a);

    default char[] nextCharArray(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return CharArrays.EMPTY_ARRAY;
        } else {
            return nextCharArray(new char[size]);
        }
    }

    char[] nextCharArray(final char @NotNull [] a);

    default short[] nextShortArray(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return ShortArrays.EMPTY_ARRAY;
        } else {
            return nextShortArray(new short[size]);
        }
    }

    short[] nextShortArray(final short @NotNull [] a);

    default int[] nextIntArray(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return IntArrays.EMPTY_ARRAY;
        } else {
            return nextIntArray(new int[size]);
        }
    }

    int[] nextIntArray(final int @NotNull [] a);

    default long[] nextLongArray(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return LongArrays.EMPTY_ARRAY;
        } else {
            return nextLongArray(new long[size]);
        }
    }

    long[] nextLongArray(final long @NotNull [] a);

    default float[] nextFloatArray(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return FloatArrays.EMPTY_ARRAY;
        } else {
            return nextFloatArray(new float[size]);
        }
    }

    default float[] nextFloatArray(final float @NotNull [] a) {
        return ArrayUtil.setAll(a, i -> nextFloat());
    }

    default double[] nextDoubleArray(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return DoubleArrays.EMPTY_ARRAY;
        } else {
            return nextDoubleArray(new double[size]);
        }
    }

    default double[] nextDoubleArray(double @NotNull [] a) {
        return ArrayUtil.setAll(a, i -> nextDouble());
    }

    default BooleanStream booleans() {
        return BooleanStream.generate(this::nextBoolean);
    }

    default BooleanStream booleans(long limit) {
        return booleans().limit(limit);
    }

    /**
     * Returns an effectively unlimited stream of pseudorandomly chosen
     * {@code byte} values.
     *
     * @return a stream of pseudorandomly chosen {@code byte} values
     *
     * @implNote It is permitted to implement this method in a manner
     * equivalent to {@link BigRandom#bytes(long) bytes}
     * ({@link Long#MAX_VALUE Long.MAX_VALUE}).
     *
     * @implSpec The default implementation produces a sequential stream
     * that repeatedly calls {@link BigRandom#nextByte() nextByte}().
     */
    default ByteStream bytes() {
        return ByteStream.generate(this::nextByte).sequential();
    }

    /**
     * Returns an effectively unlimited stream of pseudorandomly chosen
     * {@code byte} values, where each value is between the specified origin
     * (inclusive) and the specified bound (exclusive).
     *
     * @param randomNumberOrigin the least value that can be produced
     * @param randomNumberBound the upper bound (exclusive) for each value produced
     *
     * @return a stream of pseudorandomly chosen {@code byte} values, each between
     *         the specified origin (inclusive) and the specified bound (exclusive)
     *
     * @throws IllegalArgumentException if {@code randomNumberOrigin}
     *         is greater than or equal to {@code randomNumberBound}
     *
     * @implNote It is permitted to implement this method in a manner equivalent to
     * {@link BigRandom#bytes(long, byte, byte) bytes}
     * ({@link Long#MAX_VALUE Long.MAX_VALUE}, randomNumberOrigin, randomNumberBound).
     *
     * @implSpec The default implementation produces a sequential stream that repeatedly
     * calls {@link BigRandom#nextByte(byte, byte) nextByte}(randomNumberOrigin, randomNumberBound).
     */
    default ByteStream bytes(byte randomNumberOrigin, byte randomNumberBound) {
        if (randomNumberOrigin >= randomNumberBound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }

        return ByteStream.generate(() -> nextByte(randomNumberOrigin, randomNumberBound)).sequential();
    }

    /**
     * Returns a stream producing the given {@code streamSize} number of
     * pseudorandomly chosen {@code byte} values.
     *
     * @param streamSize the number of values to generate
     *
     * @return a stream of pseudorandomly chosen {@code byte} values
     *
     * @throws IllegalArgumentException if {@code streamSize} is
     *         less than zero
     *
     * @implSpec The default implementation produces a sequential stream
     * that repeatedly calls {@link BigRandom#nextByte() nextByte}().
     */
    default ByteStream bytes(long streamSize) {
        checkStreamSize(streamSize);

        return bytes().limit(streamSize);
    }

    /**
     * Returns a stream producing the given {@code streamSize} number of
     * pseudorandomly chosen {@code byte} values, where each value is between
     * the specified origin (inclusive) and the specified bound (exclusive).
     *
     * @param streamSize the number of values to generate
     * @param randomNumberOrigin the least value that can be produced
     * @param randomNumberBound the upper bound (exclusive) for each value produced
     *
     * @return a stream of pseudorandomly chosen {@code byte} values, each between
     *         the specified origin (inclusive) and the specified bound (exclusive)
     *
     * @throws IllegalArgumentException if {@code streamSize} is
     *         less than zero, or {@code randomNumberOrigin}
     *         is greater than or equal to {@code randomNumberBound}
     *
     * @implSpec The default implementation produces a sequential stream that repeatedly
     * calls {@link BigRandom#nextByte(byte, byte) nextByte}(randomNumberOrigin, randomNumberBound).
     */
    default ByteStream bytes(long streamSize, byte randomNumberOrigin,
                             byte randomNumberBound) {
        checkStreamSize(streamSize);

        if (randomNumberOrigin >= randomNumberBound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }

        return bytes(randomNumberOrigin, randomNumberBound).limit(streamSize);
    }

    /**
     * Returns an effectively unlimited stream of pseudorandomly chosen
     * {@code char} values.
     *
     * @return a stream of pseudorandomly chosen {@code char} values
     *
     * @implNote It is permitted to implement this method in a manner
     * equivalent to {@link BigRandom#chars(long) chars}
     * ({@link Long#MAX_VALUE Long.MAX_VALUE}).
     *
     * @implSpec The default implementation produces a sequential stream
     * that repeatedly calls {@link BigRandom#nextChar() nextChar}().
     */
    default CharStream chars() {
        return CharStream.generate(this::nextChar).sequential();
    }

    /**
     * Returns an effectively unlimited stream of pseudorandomly chosen
     * {@code char} values, where each value is between the specified origin
     * (inclusive) and the specified bound (exclusive).
     *
     * @param randomNumberOrigin the least value that can be produced
     * @param randomNumberBound the upper bound (exclusive) for each value produced
     *
     * @return a stream of pseudorandomly chosen {@code char} values, each between
     *         the specified origin (inclusive) and the specified bound (exclusive)
     *
     * @throws IllegalArgumentException if {@code randomNumberOrigin}
     *         is greater than or equal to {@code randomNumberBound}
     *
     * @implNote It is permitted to implement this method in a manner equivalent to
     * {@link BigRandom#chars(long, char, char) chars}
     * ({@link Long#MAX_VALUE Long.MAX_VALUE}, randomNumberOrigin, randomNumberBound).
     *
     * @implSpec The default implementation produces a sequential stream that repeatedly
     * calls {@link BigRandom#nextChar(char, char) nextChar}(randomNumberOrigin, randomNumberBound).
     */
    default CharStream chars(char randomNumberOrigin, char randomNumberBound) {
        if (randomNumberOrigin >= randomNumberBound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }

        return CharStream.generate(() -> nextChar(randomNumberOrigin, randomNumberBound)).sequential();
    }

    /**
     * Returns a stream producing the given {@code streamSize} number of
     * pseudorandomly chosen {@code char} values.
     *
     * @param streamSize the number of values to generate
     *
     * @return a stream of pseudorandomly chosen {@code char} values
     *
     * @throws IllegalArgumentException if {@code streamSize} is
     *         less than zero
     *
     * @implSpec The default implementation produces a sequential stream
     * that repeatedly calls {@link BigRandom#nextChar() nextChar}().
     */
    default CharStream chars(long streamSize) {
        checkStreamSize(streamSize);

        return chars().limit(streamSize);
    }

    /**
     * Returns a stream producing the given {@code streamSize} number of
     * pseudorandomly chosen {@code char} values, where each value is between
     * the specified origin (inclusive) and the specified bound (exclusive).
     *
     * @param streamSize the number of values to generate
     * @param randomNumberOrigin the least value that can be produced
     * @param randomNumberBound the upper bound (exclusive) for each value produced
     *
     * @return a stream of pseudorandomly chosen {@code char} values, each between
     *         the specified origin (inclusive) and the specified bound (exclusive)
     *
     * @throws IllegalArgumentException if {@code streamSize} is
     *         less than zero, or {@code randomNumberOrigin}
     *         is greater than or equal to {@code randomNumberBound}
     *
     * @implSpec The default implementation produces a sequential stream that repeatedly
     * calls {@link BigRandom#nextChar(char, char) nextChar}(randomNumberOrigin, randomNumberBound).
     */
    default CharStream chars(long streamSize, char randomNumberOrigin,
                             char randomNumberBound) {
        checkStreamSize(streamSize);

        if (randomNumberOrigin >= randomNumberBound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }

        return chars(randomNumberOrigin, randomNumberBound).limit(streamSize);
    }

    /**
     * Returns an effectively unlimited stream of pseudorandomly chosen
     * {@code short} values.
     *
     * @return a stream of pseudorandomly chosen {@code short} values
     *
     * @implNote It is permitted to implement this method in a manner
     * equivalent to {@link BigRandom#shorts(long) shorts}
     * ({@link Long#MAX_VALUE Long.MAX_VALUE}).
     *
     * @implSpec The default implementation produces a sequential stream
     * that repeatedly calls {@link BigRandom#nextShort() nextShort}().
     */
    default ShortStream shorts() {
        return ShortStream.generate(this::nextShort).sequential();
    }

    /**
     * Returns an effectively unlimited stream of pseudorandomly chosen
     * {@code short} values, where each value is between the specified origin
     * (inclusive) and the specified bound (exclusive).
     *
     * @param randomNumberOrigin the least value that can be produced
     * @param randomNumberBound the upper bound (exclusive) for each value produced
     *
     * @return a stream of pseudorandomly chosen {@code short} values, each between
     *         the specified origin (inclusive) and the specified bound (exclusive)
     *
     * @throws IllegalArgumentException if {@code randomNumberOrigin}
     *         is greater than or equal to {@code randomNumberBound}
     *
     * @implNote It is permitted to implement this method in a manner equivalent to
     * {@link BigRandom#shorts(long, short, short) shorts}
     * ({@link Long#MAX_VALUE Long.MAX_VALUE}, randomNumberOrigin, randomNumberBound).
     *
     * @implSpec The default implementation produces a sequential stream that repeatedly
     * calls {@link BigRandom#nextShort(short, short) nextShort}(randomNumberOrigin, randomNumberBound).
     */
    default ShortStream shorts(short randomNumberOrigin, short randomNumberBound) {
        if (randomNumberOrigin >= randomNumberBound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }

        return ShortStream.generate(() -> nextShort(randomNumberOrigin, randomNumberBound)).sequential();
    }

    /**
     * Returns a stream producing the given {@code streamSize} number of
     * pseudorandomly chosen {@code short} values.
     *
     * @param streamSize the number of values to generate
     *
     * @return a stream of pseudorandomly chosen {@code short} values
     *
     * @throws IllegalArgumentException if {@code streamSize} is
     *         less than zero
     *
     * @implSpec The default implementation produces a sequential stream
     * that repeatedly calls {@link BigRandom#nextShort() nextShort}().
     */
    default ShortStream shorts(long streamSize) {
        checkStreamSize(streamSize);

        return shorts().limit(streamSize);
    }

    /**
     * Returns a stream producing the given {@code streamSize} number of
     * pseudorandomly chosen {@code short} values, where each value is between
     * the specified origin (inclusive) and the specified bound (exclusive).
     *
     * @param streamSize the number of values to generate
     * @param randomNumberOrigin the least value that can be produced
     * @param randomNumberBound the upper bound (exclusive) for each value produced
     *
     * @return a stream of pseudorandomly chosen {@code short} values, each between
     *         the specified origin (inclusive) and the specified bound (exclusive)
     *
     * @throws IllegalArgumentException if {@code streamSize} is
     *         less than zero, or {@code randomNumberOrigin}
     *         is greater than or equal to {@code randomNumberBound}
     *
     * @implSpec The default implementation produces a sequential stream that repeatedly
     * calls {@link BigRandom#nextShort(short, short) nextShort}(randomNumberOrigin, randomNumberBound).
     */
    default ShortStream shorts(long streamSize, short randomNumberOrigin,
                               short randomNumberBound) {
        checkStreamSize(streamSize);

        if (randomNumberOrigin >= randomNumberBound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }

        return shorts(randomNumberOrigin, randomNumberBound).limit(streamSize);
    }

    default FloatStream floats() {
        return FloatStream.generate(this::nextFloat).sequential();
    }

    /**
     * Returns an effectively unlimited stream of pseudorandomly chosen
     * {@code float} values, where each value is between the specified origin
     * (inclusive) and the specified bound (exclusive).
     *
     * @param randomNumberOrigin the least value that can be produced
     * @param randomNumberBound the upper bound (exclusive) for each value produced
     *
     * @return a stream of pseudorandomly chosen {@code float} values, each between
     *         the specified origin (inclusive) and the specified bound (exclusive)
     *
     * @throws IllegalArgumentException if {@code randomNumberOrigin} is not finite,
     *         or {@code randomNumberBound} is not finite, or {@code randomNumberOrigin}
     *         is greater than or equal to {@code randomNumberBound}
     *
     * @implNote It is permitted to implement this method in a manner equivalent to
     * {@link BigRandom#floats(long, float, float) floats}
     * ({@link Long#MAX_VALUE Long.MAX_VALUE}, randomNumberOrigin, randomNumberBound).
     *
     * @implSpec The default implementation produces a sequential stream that repeatedly
     * calls {@link RandomGenerator#nextFloat(float, float) nextDouble}(randomNumberOrigin, randomNumberBound).
     */
    default FloatStream floats(float randomNumberOrigin, float randomNumberBound) {
        if (!(randomNumberOrigin < randomNumberBound && (randomNumberBound - randomNumberOrigin)
                < Float.POSITIVE_INFINITY)) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }

        return FloatStream.generate(() -> nextFloat(randomNumberOrigin, randomNumberBound)).sequential();
    }

    /**
     * Returns a stream producing the given {@code streamSize} number of
     * pseudorandomly chosen {@code float} values.
     *
     * @param streamSize the number of values to generate
     *
     * @return a stream of pseudorandomly chosen {@code float} values
     *
     * @throws IllegalArgumentException if {@code streamSize} is
     *         less than zero
     *
     * @implSpec The default implementation produces a sequential stream
     * that repeatedly calls {@link RandomGenerator#nextDouble nextDouble()}.
     */
    default FloatStream floats(long streamSize) {
        if (streamSize < 0L) {
            throw new IllegalArgumentException("size must be non-negative");
        }

        return floats().limit(streamSize);
    }

    /**
     * Returns a stream producing the given {@code streamSize} number of
     * pseudorandomly chosen {@code float} values, where each value is
     * between the specified origin (inclusive) and the specified bound
     * (exclusive).
     *
     * @param streamSize the number of values to generate
     * @param randomNumberOrigin the least value that can be produced
     * @param randomNumberBound the upper bound (exclusive) for each value produced
     *
     * @return a stream of pseudorandomly chosen {@code float} values, each between
     *         the specified origin (inclusive) and the specified bound (exclusive)
     *
     * @throws IllegalArgumentException if {@code streamSize} is less than zero,
     *         or {@code randomNumberOrigin} is not finite,
     *         or {@code randomNumberBound} is not finite, or {@code randomNumberOrigin}
     *         is greater than or equal to {@code randomNumberBound}
     *
     * @implSpec The default implementation produces a sequential stream that repeatedly
     * calls {@link RandomGenerator#nextFloat(float, float)  nextDouble}(randomNumberOrigin, randomNumberBound).
     */
    default FloatStream floats(long streamSize, float randomNumberOrigin,
                                 float randomNumberBound) {
        if (streamSize < 0L) {
            throw new IllegalArgumentException("size must be non-negative");
        }

        if (!(randomNumberOrigin < randomNumberBound && (randomNumberBound - randomNumberOrigin)
                < Float.POSITIVE_INFINITY)) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }

        return floats(randomNumberOrigin, randomNumberBound).limit(streamSize);
    }

    default String nextString(final int length) {
        return new String(nextCharArray(length));
    }

    default void shuffle(int from, int to, Swapper swapper) {
        Preconditions.checkPositionIndex(from, to);

        int min = from + 1;

        for (int i = to; i > min; i--) {
            swapper.swap(i - 1, nextInt(from, i));
        }
    }

    default void shuffle(Object[] a) {
        shuffle(a, 0, a.length);
    }

    default void shuffle(Object[] a, int from, int to) {
        shuffle(from, to, (i, j) -> ObjectArrays.swap(a, i, j));
    }

    default void shuffle(final boolean @NotNull [] a) {
        shuffle(a, 0, a.length);
    }

    default void shuffle(final boolean @NotNull [] a, int from, int to) {
        shuffle(from, to, (i, j) -> BooleanArrays.swap(a, i, j));
    }

    default void shuffle(final byte @NotNull [] a) {
        shuffle(a, 0, a.length);
    }

    default void shuffle(final byte @NotNull [] a, int from, int to) {
        shuffle(from, to, (i, j) -> ByteArrays.swap(a, i, j));
    }

    default void shuffle(final char @NotNull [] a) {
        shuffle(a, 0, a.length);
    }

    default void shuffle(final char @NotNull [] a, int from, int to) {
        shuffle(from, to, (i, j) -> CharArrays.swap(a, i, j));
    }

    default void shuffle(final short @NotNull [] a) {
        shuffle(a, 0, a.length);
    }

    default void shuffle(final short @NotNull [] a, int from, int to) {
        shuffle(from, to, (i, j) -> ShortArrays.swap(a, i, j));
    }

    default void shuffle(final int @NotNull [] a) {
        shuffle(a, 0, a.length);
    }

    default void shuffle(final int @NotNull [] a, int from, int to) {
        shuffle(from, to, (i, j) -> IntArrays.swap(a, i, j));
    }

    default void shuffle(final long @NotNull [] a) {
        shuffle(a, 0, a.length);
    }

    default void shuffle(final long @NotNull [] a, int from, int to) {
        shuffle(from, to, (i, j) -> LongArrays.swap(a, i, j));
    }

    default void shuffle(final float @NotNull [] a) {
        shuffle(a, 0, a.length);
    }

    default void shuffle(final float @NotNull [] a, int from, int to) {
        shuffle(from, to, (i, j) -> FloatArrays.swap(a, i, j));
    }

    default void shuffle(final double @NotNull [] a) {
        shuffle(a, 0, a.length);
    }

    default void shuffle(final double @NotNull [] a, int from, int to) {
        shuffle(from, to, (i, j) -> DoubleArrays.swap(a, i, j));
    }

    @SuppressWarnings("rawuse")
    default <E> void shuffle(List<E> list) {
        int size = list.size();

        if (size < 5 || list instanceof RandomAccess) {
            shuffle(0, list.size(), (i, j) -> Collections.swap(list, i, j));
        } else {
            Object[] arr = list.toArray();

            shuffle(arr);

            // Dump array back into list
            // instead of using a raw type here, it's possible to capture
            // the wildcard but it will require a call to a supplementary
            // private method
            ListIterator it = list.listIterator();
            for (Object e : arr) {
                it.next();
                it.set(e);
            }
        }
    }

    default <E> E randElement(Collection<E> collection) {
        if (collection instanceof List<E>) {
            return randElement((List<? extends E>) collection);
        }

        int i = nextInt(collection.size());
        Iterator<E> it = collection.iterator();

        try {
            while (i-- > 0) {
                it.next();
            }
        } catch (NoSuchElementException e) {
            throw new ConcurrentModificationException(e);
        }

        return it.next();
    }

    default <E> E randElement(List<E> list) {
        return list.get(nextInt(list.size()));
    }

    default <E> E randElement(E[] array) {
        return array[nextInt(array.length)];
    }

    static void checkStreamSize(long streamSize) {
        if (streamSize < 0L) {
            throw new IllegalArgumentException("size must be non-negative");
        }
    }
}
