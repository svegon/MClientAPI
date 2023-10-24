package com.github.svegon.utils.math.really_big_math.random;

import it.unimi.dsi.fastutil.Swapper;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.NotNull;

@ThreadSafe
public final class SynchronizedJavaStandardBigRandom extends JavaStandardBigRandom {
    public SynchronizedJavaStandardBigRandom(int[] seed) {
        super(seed);
    }

    public SynchronizedJavaStandardBigRandom(int seedSize) {
        super(seedSize);
    }

    @Override
    public SynchronizedJavaStandardBigRandom clone() {
        return new SynchronizedJavaStandardBigRandom(seed.getAcquire().clone());
    }

    @Override
    public synchronized void setSeed(int @NotNull [] seed) {
        super.setSeed(seed);
    }

    @Override
    public synchronized double nextDouble() {
        return super.nextDouble();
    }

    @Override
    public synchronized boolean[] nextBooleanArray(final boolean @NotNull [] a) {
        return super.nextBooleanArray(a);
    }

    @Override
    public synchronized byte[] nextByteArray(final byte @NotNull [] a) {
        return super.nextByteArray(a);
    }

    @Override
    public synchronized char[] nextCharArray(final char @NotNull [] a) {
        return super.nextCharArray(a);
    }

    @Override
    public synchronized short[] nextShortArray(final short @NotNull [] a) {
        return super.nextShortArray(a);
    }

    @Override
    public synchronized int[] nextIntArray(final int @NotNull [] a) {
        return super.nextIntArray(a);
    }

    @Override
    public synchronized long[] nextLongArray(final long @NotNull [] a) {
        return super.nextLongArray(a);
    }

    @Override
    public synchronized float[] nextFloatArray(float @NotNull [] a) {
        return super.nextFloatArray(a);
    }

    @Override
    public synchronized double[] nextDoubleArray(double @NotNull [] a) {
        return super.nextDoubleArray(a);
    }

    @Override
    public synchronized void shuffle(int from, int to, Swapper swapper) {
        super.shuffle(from, to, swapper);
    }

    @Override
    protected synchronized int[] nextSeed() {
        return super.nextSeed();
    }
}
