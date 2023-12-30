package io.github.svegon.utils;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public final class AtomicUtil {
    private AtomicUtil() {
        throw new UnsupportedOperationException();
    }

    public static void arrayCopy(AtomicIntegerArray src, int srcOffset, AtomicIntegerArray dst, int dstOffset,
                                 int length) {
        int end = srcOffset + length;
        int offset = dstOffset;

        for (int i = srcOffset; i < end; i++) {
            dst.set(offset++, src.get(i));
        }
    }

    public static void arrayCopy(int[] src, int srcOffset, AtomicIntegerArray dst, int dstOffset, int length) {
        int end = srcOffset + length;
        int offset = dstOffset;

        for (int i = srcOffset; i < end; i++) {
            dst.set(offset++, src[i]);
        }
    }

    public static void arrayCopy(AtomicIntegerArray src, int srcOffset, int[] dst, int dstOffset, int length) {
        int end = srcOffset + length;
        int offset = dstOffset;

        for (int i = srcOffset; i < end; i++) {
            dst[offset++] = src.get(i);
        }
    }

    public static void fill(AtomicIntegerArray src, int value) {
        fill(src, 0, src.length(), value);
    }

    public static void fill(AtomicIntegerArray src, int from, int to, int value) {
        Preconditions.checkPositionIndexes(from, to, src.length());

        for (int i = from; i < to; i++) {
            src.set(i, value);
        }
    }

    public static int[] clone(final AtomicIntegerArray atomic) {
        int[] a = new int[atomic.length()];

        Arrays.setAll(a, atomic::get);

        return a;
    }

    public static AtomicIntegerArray setAll(AtomicIntegerArray a, IntUnaryOperator generator) {
        return setRange(a, 0, a.length(), generator);
    }

    public static AtomicIntegerArray setRange(AtomicIntegerArray a, int from, int to, IntUnaryOperator generator) {
        Preconditions.checkPositionIndexes(from, to, a.length());
        Preconditions.checkNotNull(generator);

        for (int i = from; i < to; i++) {
            a.set(i, generator.applyAsInt(i));
        }

        return a;
    }

    public static AtomicIntegerArray updateAll(AtomicIntegerArray a, IntUnaryOperator updateFunction) {
        return updateRange(a, 0, a.length(), updateFunction);
    }

    public static AtomicIntegerArray updateRange(AtomicIntegerArray a, int from, int to,
                                                 IntUnaryOperator updateFunction) {
        Preconditions.checkPositionIndexes(from, to, a.length());
        Preconditions.checkNotNull(updateFunction);

        for (int i = from; i < to; i++) {
            a.getAndUpdate(i, updateFunction);
        }

        return a;
    }

    public static AtomicIntegerArray accumulateAll(AtomicIntegerArray a, IntUnaryOperator generator,
                                                   IntBinaryOperator accumulator) {
        return accumulateRange(a, 0, a.length(), generator, accumulator);
    }

    public static AtomicIntegerArray accumulateRange(AtomicIntegerArray a, int from, int to, IntUnaryOperator generator,
                                                     IntBinaryOperator accumulator) {
        Preconditions.checkPositionIndexes(from, to, a.length());
        Preconditions.checkNotNull(generator);
        Preconditions.checkNotNull(accumulator);

        for (int i = from; i < to; i++) {
            a.getAndAccumulate(i, generator.applyAsInt(i), accumulator);
        }

        return a;
    }
}
