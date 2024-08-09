package io.github.svegon.utils.math.really_big_math.random;

import io.github.svegon.utils.collections.ArrayUtil;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public abstract class JavaStandardBigRandom implements BigRandom, Cloneable {
    private static final long MULTIPLIER = 0x5DEECE66DL;
    private static final long LONG_MASK = 0xFFFFFFFFL;
    protected static final int[] MULTIPLIER_PATTERN = new int[]{(int) (MULTIPLIER >>> 16),
            (int) ((MULTIPLIER << 16) | (MULTIPLIER >>> 32)), (int) MULTIPLIER};
    protected static final int[] ADDITION_PATTERN = new int[]{0, 11 << 16, 11};

    protected final AtomicReference<int[]> seed;

    JavaStandardBigRandom(AtomicReference<int[]> seed) {
        if (seed.get().length < 2) {
            throw new IllegalArgumentException("illegal seed size: " + seed.get().length);
        }

        this.seed = seed;
    }

    JavaStandardBigRandom(int[] seed) {
        this(new AtomicReference<>(seed.length % 3 != 0
                ? Arrays.copyOf(seed, seed.length + 3 - seed.length % 3) : seed.clone()));
    }

    JavaStandardBigRandom(int seedSize) {
        this(new AtomicReference<>(new int[seedSize % 3 == 0 ? seedSize : seedSize + 3 - seedSize % 3]));

        initRandomSeed();
    }

    @Override
    public abstract JavaStandardBigRandom clone();

    @Override
    public int[] getSeed() {
        return seed.get().clone();
    }

    @Override
    public void setSeed(final int @NotNull [] seed) {
        final int unused = this.seed.get().length - seed.length;

        if (unused > 0) {
            final int copyThreshold = unused - 3;
            int copyIndex;

            for (copyIndex = 0; copyIndex < copyThreshold; copyIndex += 3) {
                System.arraycopy(MULTIPLIER_PATTERN, 0, this.seed.get(), copyIndex, 3);
            }

            System.arraycopy(MULTIPLIER_PATTERN, 0, this.seed.get(), copyIndex, unused - copyIndex);
            ArrayUtil.setRange(this.seed.get(), (i) -> seed[i] ^ MULTIPLIER_PATTERN[i % 3], 0, seed.length);
        } else {
            ArrayUtil.setAll(this.seed.get(), i -> seed[i + unused] ^ MULTIPLIER_PATTERN[i % 3]);
        }
    }

    @Override
    public int nextInt() {
        return nextSeed()[0];
    }

    @Override
    public long nextLong() {
        int[] seed = nextSeed();
        return ((long) seed[0] << 32L) + seed[1];
    }

    @Override
    public boolean[] nextBooleanArray(final boolean @NotNull [] a) {
        int[] seed = this.seed.get();
        final int length = seed.length;
        int j = length;
        int offset = 0;
        int i;

        while (true) {
            if (j == length) {
                seed = nextSeed();
                j = 0;
            }

            int e = seed[j++];

            while ((i = Integer.numberOfLeadingZeros(e)) < Integer.SIZE) {
                int index = offset + i;

                if (index >= a.length) {
                    return a;
                }

                a[index] = true;
                e &= ~(0x80000000 >>> i);
            }

            offset += Integer.SIZE;
        }
    }

    @Override
    public byte[] nextByteArray(byte @NotNull [] a) {
        final int length = this.seed.get().length;
        int i = 0;
        int j = 0;
        int[] seed = this.seed.get();

        while (i < a.length) {
            if (j == length) {
                seed = nextSeed();
                j = 0;
            }

            a[i++] = (byte) switch (i & 3) {
                case 0 -> seed[j] >>> 24;
                case 1 -> seed[j] >>> 16;
                case 2 -> seed[j] >>> 8;
                case 3 -> seed[j++];
                default -> throw new IllegalStateException("Unexpected value: " + (i & 3));
            };
        }

        return a;
    }

    @Override
    public char[] nextCharArray(char @NotNull [] a) {
        final int size = a.length;
        final int length = this.seed.get().length;
        int i = 0;
        int j = 0;
        int[] seed = nextSeed();

        while (i < size) {
            if (j == length) {
                seed = nextSeed();
                j = 0;
            }

            if ((i & 1) != 0) {
                a[i++] = (char) seed[j++];
            } else {
                a[i++] = (char) (seed[j] >>> 16);
            }
        }

        return a;
    }

    @Override
    public short[] nextShortArray(final short @NotNull [] a) {
        final int size = a.length;
        final int length = this.seed.get().length;
        int i = 0;
        int j = 0;
        int[] seed = nextSeed();

        while (i < size) {
            if (j == length) {
                seed = nextSeed();
                j = 0;
            }

            if ((i & 1) != 0) {
                a[i++] = (short) seed[j++];
            } else {
                a[i++] = (short) (seed[j] >>> 16);
            }
        }

        return a;
    }

    @Override
    public int[] nextIntArray(final int @NotNull [] a) {
        final int size = a.length;
        final int length = seed.get().length;
        final int end = size - length;
        int i = 0;

        for (; i < end; i += length) {
            System.arraycopy(nextSeed(), 0, a, i, length);
        }

        System.arraycopy(nextSeed(), 0, a, i, size - i);

        return a;
    }

    @Override
    public long[] nextLongArray(final long @NotNull [] a) {
        final int length = this.seed.get().length;
        int[] seed = this.seed.get();
        int j = length;

        if ((length & 1) == 0) {
            for (int i = 0; i < a.length; i++) {
                if (j == length) {
                    seed = nextSeed();
                    j = 0;
                }

                a[i] = ((long) seed[j++] << 32) + seed[j++];
            }
        } else {
            for (int i = 0; i < a.length;) {
                if (j == length) {
                    seed = nextSeed();
                    j = 0;
                }

                if ((j & 1) == 0) {
                    a[i] = ((long) seed[j++] << 32);
                } else {
                    a[i++] += seed[j++];
                }
            }
        }

        return a;
    }

    protected int[] nextSeed() {
        return seed.updateAndGet(JavaStandardBigRandom::nextSeed);
    }

    public final void initRandomSeed() {
        SecureRandom rnd = new SecureRandom();
        ArrayUtil.setAll(seed.get(), i -> rnd.nextInt());
    }

    public static ConcurrentJavaStandardBigRandom create(int size) {
        return new ConcurrentJavaStandardBigRandom(size);
    }

    public static ConcurrentJavaStandardBigRandom create(int[] seed) {
        return new ConcurrentJavaStandardBigRandom(seed);
    }

    public static SynchronizedJavaStandardBigRandom createSynchronized(int size) {
        return new SynchronizedJavaStandardBigRandom(size);
    }

    public static SynchronizedJavaStandardBigRandom createSynchronized(int[] seed) {
        return new SynchronizedJavaStandardBigRandom(seed);
    }

    /**
     * based on {@linkplain java.math.BigInteger} Karatsuba multiply
     * @param seed
     * @param multiplier
     */
    @Contract(pure = true)
    public static int @NotNull [] multiply(int[] seed, int[] multiplier) {
        int start = seed.length - 1;
        long carry = 0;
        int[] result = new int[seed.length];

        for (int j = start; j >= 0; j--) {
            long product = (multiplier[j] & LONG_MASK) * (seed[start] & LONG_MASK) + carry;
            result[j] = (int) product;
            carry = product >>> 32;
        }

        result[start] = (int) carry;

        for (int i = start-1; i >= 0; i--) {
            carry = 0;

            for (int j = start; j >= 0; j--) {
                long product = (multiplier[j] & LONG_MASK) * (seed[i] & LONG_MASK) + (result[j] & LONG_MASK) + carry;
                result[j] = (int)product;
                carry = product >>> 32;
            }
        }

        return result;
    }

    @Contract(pure = true)
    public static int @NotNull [] multiply(int[] seed) {
        int start = seed.length - 1;
        long carry = 0;
        int[] result = new int[seed.length];

        for (int j = start; j >= 0; j--) {
            long product = (MULTIPLIER_PATTERN[j % 3] & LONG_MASK) * (seed[start] & LONG_MASK) + carry;
            result[j] = (int) product;
            carry = product >>> 32;
        }

        result[start] = (int) carry;

        for (int i = start-1; i >= 0; i--) {
            carry = 0;

            for (int j = start; j >= 0; j--) {
                long product = (MULTIPLIER_PATTERN[j % 3] & LONG_MASK) * (seed[i] & LONG_MASK)
                        + (result[j] & LONG_MASK) + carry;
                result[j] = (int)product;
                carry = product >>> 32;
            }
        }

        return result;
    }

    @Contract("_, _ -> param1")
    public static int[] inPlaceAdd(int[] num, int[] addition) {
        long carry = 0;

        for (int i = 0; i < num.length; i++) {
            long sum = (num[i] & LONG_MASK) + (addition[i] & LONG_MASK) + carry;
            num[i] = (int) sum;
            carry = sum >>> 32;
        }

        return num;
    }

    @Contract("_ -> param1")
    public static int[] inPlaceAdd(int[] num) {
        long carry = 0;

        for (int i = 0; i < num.length; i++) {
            long sum = (num[i] & LONG_MASK) + (ADDITION_PATTERN[i % 3] & LONG_MASK) + carry;
            num[i] = (int) sum;
            carry = sum >>> 32;
        }

        return num;
    }

    @Contract(pure = true)
    public static int[] nextSeed(int[] seed) {
        return inPlaceAdd(multiply(seed));
    }
}
