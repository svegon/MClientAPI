package io.github.svegon.utils.math;

import io.github.svegon.utils.collections.ArrayUtil;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.chars.CharArrays;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.floats.FloatArrays;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.longs.LongArrays;
import it.unimi.dsi.fastutil.shorts.ShortArrays;
import org.jetbrains.annotations.NotNull;

import java.util.random.RandomGenerator;

public final class RandomUtil {
    private RandomUtil() {
        assert false;
        throw new UnsupportedOperationException();
    }

    public static boolean nextBoolean(RandomGenerator random, double chance) {
        return random.nextDouble() < chance;
    }

    public static boolean nextBoolean(RandomGenerator random, int inverseChance) {
        return random.nextInt(inverseChance) == 0;
    }

    public static byte nextByte(RandomGenerator random) {
        return (byte) (random.nextInt() >>> 24);
    }

    public static byte nextByte(RandomGenerator random, byte bound) {
        return (byte) random.nextInt(bound);
    }

    public static byte nextByte(RandomGenerator random, byte origin, byte bound) {
        return (byte) random.nextInt(origin, bound);
    }

    public static char nextChar(RandomGenerator random) {
        return (char) (random.nextInt() >> 16);
    }

    public static char nextChar(RandomGenerator random, char bound) {
        return (char) random.nextInt(bound);
    }

    public static char nextChar(RandomGenerator random, char origin, char bound) {
        return (char) random.nextInt(origin, bound);
    }

    public static short nextShort(RandomGenerator random) {
        return (short) (random.nextInt() >> 16);
    }

    public static short nextShort(RandomGenerator random, short bound) {
        return (short) random.nextInt(bound);
    }

    public static short nextShort(RandomGenerator random, short origin, short bound) {
        return (short) random.nextInt(origin, bound);
    }

    public static boolean[] nextBooleanArray(RandomGenerator random, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return BooleanArrays.EMPTY_ARRAY;
        } else {
            return nextBooleanArray(random, new boolean[size]);
        }
    }

    public static boolean[] nextBooleanArray(final @NotNull RandomGenerator random, final boolean @NotNull [] a) {
        int offset = 0;
        int i;

        while (true) {
            long e = random.nextLong();

            while ((i = Long.numberOfLeadingZeros(e)) < Long.SIZE) {
                int index = offset + i;

                if (index >= a.length) {
                    return a;
                }

                a[index] = true;
                e &= ~(Long.MIN_VALUE >>> i);
            }

            offset += Long.SIZE;
        }
    }

    public static byte[] nextByteArray(final @NotNull RandomGenerator random, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return ByteArrays.EMPTY_ARRAY;
        } else {
            return nextByteArray(random, new byte[size]);
        }
    }

    public static byte[] nextByteArray(final @NotNull RandomGenerator random, final byte @NotNull [] a) {
        random.nextBytes(a);
        return a;
    }

    public static char[] nextCharArray(final @NotNull RandomGenerator random, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return CharArrays.EMPTY_ARRAY;
        } else {
            return nextCharArray(random, new char[size]);
        }
    }

    public static char[] nextCharArray(final @NotNull RandomGenerator random, char @NotNull [] a) {
        int i = 0;
        long l = 0;

        while (i < a.length) {
            a[i++] = (char) switch (i & 3) {
                case 0 -> {
                    l = random.nextLong();
                    yield l >>> 48;
                }
                case 1 -> l >>> 32;
                case 2 -> l >>> 16;
                case 3 -> l;
                default -> throw new IllegalStateException("Unexpected value: " + (i & 3));
            };
        }

        return a;
    }

    public static short[] nextShortArray(final @NotNull RandomGenerator random, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return ShortArrays.EMPTY_ARRAY;
        } else {
            return nextShortArray(random, new short[size]);
        }
    }

    public static short[] nextShortArray(final @NotNull RandomGenerator random, final short @NotNull [] a) {
        int i = 0;
        long l = 0;

        while (i < a.length) {
            a[i] = (short) switch (i++ & 3) {
                case 0 -> {
                    l = random.nextLong();
                    yield l >>> 48;
                }
                case 1 -> l >>> 32;
                case 2 -> l >>> 16;
                case 3 -> l;
                default -> throw new IllegalStateException("Unexpected value: " + (i & 3));
            };
        }

        return a;
    }

    public static int[] nextIntArray(final @NotNull RandomGenerator random, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return IntArrays.EMPTY_ARRAY;
        } else {
            return nextIntArray(random, new int[size]);
        }
    }

    public static int[] nextIntArray(final @NotNull RandomGenerator random, final int @NotNull [] a) {
        final int size = a.length;
        final int end = size - 1;
        int i = 0;

        while (i < end) {
            long n = random.nextLong();
            a[i++] = (int) (n >>> 32);
            a[i++] = (int) n;
        }

        if (i < size) {
            a[i] = random.nextInt();
        }

        return a;
    }

    public static long[] nextLongArray(final @NotNull RandomGenerator random, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return LongArrays.EMPTY_ARRAY;
        } else {
            return nextLongArray(random, new long[size]);
        }
    }

    public static long[] nextLongArray(final @NotNull RandomGenerator random, long @NotNull [] a) {
        return ArrayUtil.setAll(a, i -> random.nextLong());
    }

    public static float[] nextFloatArray(final @NotNull RandomGenerator random, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return FloatArrays.EMPTY_ARRAY;
        } else {
            return nextFloatArray(random, new float[size]);
        }
    }

    public static float[] nextFloatArray(final @NotNull RandomGenerator random, float @NotNull [] a) {
        return ArrayUtil.setAll(a, i -> random.nextFloat());
    }

    public static double[] nextDoubleArray(final @NotNull RandomGenerator random, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("negative array size: " + size);
        } else if (size == 0) {
            return DoubleArrays.EMPTY_ARRAY;
        } else {
            return nextDoubleArray(random, new double[size]);
        }
    }

    public static double[] nextDoubleArray(final @NotNull RandomGenerator random, double @NotNull [] a) {
        return ArrayUtil.setAll(a, i -> random.nextDouble());
    }
}
