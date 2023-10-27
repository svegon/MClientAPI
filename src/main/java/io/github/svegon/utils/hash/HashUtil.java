package io.github.svegon.utils.hash;

import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.collections.ListUtil;
import io.github.svegon.utils.collections.MapUtil;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.booleans.BooleanHash;
import it.unimi.dsi.fastutil.bytes.ByteHash;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.chars.CharHash;
import it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import it.unimi.dsi.fastutil.doubles.DoubleHash;
import it.unimi.dsi.fastutil.floats.FloatHash;
import it.unimi.dsi.fastutil.ints.IntHash;
import it.unimi.dsi.fastutil.ints.IntIterable;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.longs.LongHash;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.shorts.ShortHash;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.function.LongToIntFunction;

public final class HashUtil {
    private HashUtil() {
        throw new UnsupportedOperationException();
    }

    private static final Hash.Strategy<Object> IDENTITY_STRATEGY = new Hash.Strategy<>() {
        @Override
        public int hashCode(Object o) {
            return System.identityHashCode(o);
        }

        @Override
        public boolean equals(Object a, Object b) {
            return a == b;
        }
    };
    private static final Hash.Strategy<Object> ARRAY_REGARDING_STRATEGY = new ArrayRegardingStrategy();
    private static final Hash.Strategy<String> CASE_IGNORING_STRATEGY = new CaseIgnoringStrategy();
    private static final Hash.Strategy<Object> DEFAULT_OBJECT_STRATEGY = new Hash.Strategy<>() {
        @Override
        public int hashCode(Object o) {
            return Objects.hashCode(o);
        }

        @Override
        public boolean equals(Object a, Object b) {
            return Objects.equals(a, b);
        }
    };
    private static final BooleanHash.Strategy DEFAULT_BOOLEAN_STRATEGY = new BooleanHash.Strategy() {
        @Override
        public int hashCode(boolean e) {
            return Boolean.hashCode(e);
        }

        @Override
        public boolean equals(boolean a, boolean b) {
            return a == b;
        }
    };
    private static final ByteHash.Strategy DEFAULT_BYTE_STRATEGY = new ByteHash.Strategy() {
        @Override
        public int hashCode(byte e) {
            return Byte.hashCode(e);
        }

        @Override
        public boolean equals(byte a, byte b) {
            return a == b;
        }
    };
    private static final ShortHash.Strategy DEFAULT_SHORT_STRATEGY = new ShortHash.Strategy() {
        @Override
        public int hashCode(short e) {
            return Short.hashCode(e);
        }

        @Override
        public boolean equals(short a, short b) {
            return a == b;
        }
    };
    private static final IntHash.Strategy DEFAULT_INT_STRATEGY = new IntHash.Strategy() {
        @Override
        public int hashCode(int e) {
            return Integer.hashCode(e);
        }

        @Override
        public boolean equals(int a, int b) {
            return a == b;
        }
    };
    private static final LongHash.Strategy DEFAULT_LONG_STRATEGY = new LongHash.Strategy() {
        @Override
        public int hashCode(long e) {
            return Long.hashCode(e);
        }

        @Override
        public boolean equals(long a, long b) {
            return a == b;
        }
    };
    private static final CharHash.Strategy DEFAULT_CHAR_STRATEGY =  new CharHash.Strategy() {
        @Override
        public int hashCode(char e) {
            return Character.hashCode(e);
        }

        @Override
        public boolean equals(char a, char b) {
            return a == b;
        }
    };
    private static final FloatHash.Strategy DEFAULT_FLOAT_STRATEGY = new FloatHash.Strategy() {
        @Override
        public int hashCode(float e) {
            return Float.hashCode(e);
        }

        @Override
        public boolean equals(float a, float b) {
            return Float.compare(a, b) == 0;
        }
    };
    private static final DoubleHash.Strategy DEFAULT_DOUBLE_STRATEGY = new DoubleHash.Strategy() {
        @Override
        public int hashCode(double e) {
            return Double.hashCode(e);
        }

        @Override
        public boolean equals(double a, double b) {
            return Double.compare(a, b) == 0;
        }
    };

    public static Hash.Strategy<Object> defaultStrategy() {
        return DEFAULT_OBJECT_STRATEGY;
    }

    public static BooleanHash.Strategy defaultBooleanStrategy() {
        return DEFAULT_BOOLEAN_STRATEGY;
    }

    public static ByteHash.Strategy defaultByteStrategy() {
        return DEFAULT_BYTE_STRATEGY;
    }

    public static ShortHash.Strategy defaultShortStrategy() {
        return DEFAULT_SHORT_STRATEGY;
    }

    public static IntHash.Strategy defaultIntStrategy() {
        return DEFAULT_INT_STRATEGY;
    }

    public static LongHash.Strategy defaultLongStrategy() {
        return DEFAULT_LONG_STRATEGY;
    }

    public static CharHash.Strategy defaultCharStrategy() {
        return DEFAULT_CHAR_STRATEGY;
    }

    public static FloatHash.Strategy defaultFloatStrategy() {
        return DEFAULT_FLOAT_STRATEGY;
    }

    public static DoubleHash.Strategy defaultDoubleStrategy() {
        return DEFAULT_DOUBLE_STRATEGY;
    }

    public static Hash.Strategy<Object> arrayRegardingStrategy() {
        return ARRAY_REGARDING_STRATEGY;
    }

    public static Hash.Strategy<Object> identityStrategy() {
        return IDENTITY_STRATEGY;
    }

    public static Hash.Strategy<String> caseIgnoringStringHash() {
        return CASE_IGNORING_STRATEGY;
    }

    /**
     * combines hash codes supplied by the given iterator with regard to their order
     *
     * @param hashIterator the iterator supplying the hash codes
     * @return resulting hash code
     */
    public static int hashOrdered(@NotNull IntIterator hashIterator) {
        int h = 0;

        while (hashIterator.hasNext()) {
            h = h * 31 + hashIterator.nextInt();
        }

        return h;
    }

    public static int hashOrdered(@NotNull IntIterable hashCollection) {
        return hashOrdered(hashCollection.iterator());
    }

    public static int hashOrdered(final @NotNull IntHash.Strategy strategy, int @NotNull ... values) {
        return hashOrdered(ListUtil.mapToInt(ArrayUtil.asList(values), (IntUnaryOperator) strategy::hashCode));
    }

    public static int hashOrdered(int @NotNull ... values) {
        return hashOrdered(defaultIntStrategy(), values);
    }

    public static int hashOrdered(final @NotNull LongHash.Strategy strategy, long @NotNull ... values) {
        return hashOrdered(ListUtil.mapToInt(ArrayUtil.asList(values), (LongToIntFunction) strategy::hashCode));
    }

    public static int hashOrdered(final long @NotNull ... values) {
        return hashOrdered(defaultLongStrategy(), values);
    }

    public static int hashOrdered(final @NotNull DoubleHash.Strategy strategy, final double @NotNull ... values) {
        return hashOrdered(ListUtil.mapToInt(ArrayUtil.asList(values), (Double2IntFunction) strategy::hashCode));
    }

    public static int hashOrdered(final double @NotNull ... values) {
        return hashOrdered(defaultDoubleStrategy(), values);
    }

    /**
     * combines hash codes supplied by the given iterator with regard to their order
     *
     * @param hashIterator the iterator supplying the hash codes
     * @return resulting hash code
     */
    public static int hashUnordered(@NotNull IntIterator hashIterator) {
        int h = 0;

        while (hashIterator.hasNext()) {
            h ^= hashIterator.nextInt();
        }

        return h;
    }

    public static int hashUnordered(@NotNull IntIterable hashCollection) {
        return hashUnordered(hashCollection.iterator());
    }

    public static int hashUnordered(@NotNull IntHash.Strategy strategy, int @NotNull ... values) {
        return hashUnordered(ListUtil.mapToInt(ArrayUtil.asList(values), strategy::hashCode));
    }

    public static int hashUnordered(int @NotNull ... values) {
        return hashUnordered(defaultIntStrategy(), values);
    }

    public static int hashUnordered(final @NotNull LongHash.Strategy strategy, long @NotNull ... values) {
        return hashUnordered(ListUtil.mapToInt(ArrayUtil.asList(values), strategy::hashCode));
    }

    public static int hashUnordered(long @NotNull ... values) {
        return hashUnordered(defaultLongStrategy(), values);
    }

    public static int hashUnordered(final @NotNull DoubleHash.Strategy strategy, double @NotNull ... values) {
        return hashUnordered(ListUtil.mapToInt(ArrayUtil.asList(values),
                strategy::hashCode));
    }

    public static int hashUnordered(double @NotNull ... values) {
        return hashUnordered(defaultDoubleStrategy(), values);
    }

    public static int hash(final @NotNull ByteList list, int from, int to) {
        int h = 1;

        while (from < to) {
            h = 31 * h + list.getByte(from++);
        }

        return h;
    }

    private static final class CaseIgnoringStrategy implements Hash.Strategy<String> {
        final Object2IntMap<String> hashCodeCache =
                MapUtil.derefWeakRefK(new Object2IntOpenCustomHashMap<>(new Hash.Strategy<>() {
                    @Override
                    public int hashCode(WeakReference<String> o) {
                        return o == null ? 0 : System.identityHashCode(o.get());
                    }

                    @Override
                    public boolean equals(WeakReference<String> a, WeakReference<String> b) {
                        return a == null ? b == null : b != null && a.get() == b.get();
                    }
                }));

        private CaseIgnoringStrategy() {
            hashCodeCache.put(null, 0);
        }

        @Override
        public int hashCode(String o) {
            return hashCodeCache.computeIfAbsent(o, (String str) -> {
                int length = str.length();
                int h = 0;

                for (int i = 0; i < length; i++) {
                    h = 31 * h + Character.toUpperCase(str.codePointAt(i));
                }

                return h;
            });
        }

        @Override
        public boolean equals(String a, String b) {
            return a == b || (a != null && a.equalsIgnoreCase(b));
        }
    }

    private static final class ArrayRegardingStrategy implements Hash.Strategy<Object> {
        @Override
        public int hashCode(Object o) {
            if (o == null) {
                return 0;
            }

            if (!o.getClass().isArray()) {
                return o.hashCode();
            }

            if (o instanceof boolean[]) {
                return Arrays.hashCode((boolean[]) o);
            }

            if (o instanceof byte[]) {
                return Arrays.hashCode((byte[]) o);
            }

            if (o instanceof char[]) {
                return Arrays.hashCode((char[]) o);
            }

            if (o instanceof short[]) {
                return Arrays.hashCode((short[]) o);
            }

            if (o instanceof int[]) {
                return Arrays.hashCode((int[]) o);
            }

            if (o instanceof long[]) {
                return Arrays.hashCode((long[]) o);
            }

            if (o instanceof float[]) {
                return Arrays.hashCode((float[]) o);
            }

            if (o instanceof double[]) {
                return Arrays.hashCode((double[]) o);
            }

            return Arrays.deepHashCode((Object[]) o);
        }

        @Override
        public boolean equals(Object a, Object b) {
            if (a == b) {
                return true;
            }

            if (a == null) {
                return false;
            }

            if (!a.getClass().isArray()) {
                return a.equals(b);
            }

            if (a instanceof boolean[]) {
                return b instanceof boolean[] && Arrays.equals((boolean[]) a, (boolean[]) b);
            }

            if (a instanceof byte[]) {
                return b instanceof byte[] && Arrays.equals((byte[]) a, (byte[]) b);
            }

            if (a instanceof char[]) {
                return b instanceof char[] && Arrays.equals((char[]) a, (char[]) b);
            }

            if (a instanceof short[]) {
                return b instanceof short[] && Arrays.equals((short[]) a, (short[]) b);
            }

            if (a instanceof int[]) {
                return b instanceof int[] && Arrays.equals((int[]) a, (int[]) b);
            }

            if (a instanceof long[]) {
                return b instanceof long[] && Arrays.equals((long[]) a, (long[]) b);
            }

            if (a instanceof float[]) {
                return b instanceof float[] && Arrays.equals((float[]) a, (float[]) b);
            }

            if (a instanceof double[]) {
                return b instanceof double[] && Arrays.equals((double[]) a, (double[]) b);
            }

            return b.getClass().isArray() && !b.getClass().componentType().isPrimitive()
                    && Arrays.deepEquals((Object[]) a, (Object[]) b);
        }
    }
}
