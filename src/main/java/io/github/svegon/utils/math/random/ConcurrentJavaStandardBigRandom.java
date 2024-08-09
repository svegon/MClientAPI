package io.github.svegon.utils.math.random;

public final class ConcurrentJavaStandardBigRandom extends JavaStandardBigRandom {
    public ConcurrentJavaStandardBigRandom(int[] seed) {
        super(seed);
    }

    public ConcurrentJavaStandardBigRandom(int seedSize) {
        super(seedSize);
    }

    @Override
    public ConcurrentJavaStandardBigRandom clone() {
        return new ConcurrentJavaStandardBigRandom(seed.get());
    }
}
