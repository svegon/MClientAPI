package io.github.svegon.utils.math;

import it.unimi.dsi.fastutil.SafeMath;
import org.jetbrains.annotations.Range;

public enum MathUtil {
    ;

    public static final float PI = (float) Math.PI;
    public static final float DEGREE_TO_RAD_RATIO = PI / 180F;
    public static final int NEGATIVE_ZERO_FLOAT_BITS = Float.floatToRawIntBits(-0.0f);
    public static final long NEGATIVE_ZERO_DOUBLE_BITS = Double.doubleToRawLongBits(-0.0d);
    public static final long DOUBLE_CANONICAL_NaN_BITS = Double.doubleToLongBits(Double.NaN);

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public static float safeLongToFloat(long l) {
        float f = l;

        if (f != l) {
            throw new IllegalArgumentException(l + " can't be represented as int (insufficient precision)");
        }

        return f;
    }

    public static double safeLongToDouble(long l) {
        double d = l;

        if (d != l) {
            throw new IllegalArgumentException(l + " can't be represented as int (insufficient precision)");
        }

        return d;
    }

    public static byte safeFloatToByte(float f) {
        return SafeMath.safeIntToByte(safeFloatToInt(f));
    }

    public static char safeFloatToChar(float f) {
        return SafeMath.safeIntToChar(safeFloatToInt(f));
    }

    public static short safeFloatToShort(float f) {
        return SafeMath.safeIntToShort(safeFloatToInt(f));
    }

    public static int safeFloatToInt(float f) {
        int i = (int) f;

        if (f != i) {
            throw new IllegalArgumentException(f + " can't be represented as int (out of range)");
        }

        return i;
    }

    public static long safeFloatToLong(float f) {
        long i = (long) f;

        if (f != i) {
            throw new IllegalArgumentException(f + " can't be represented as int (out of range)");
        }

        return i;
    }

    public static byte safeDoubleToByte(double d) {
        return SafeMath.safeIntToByte(safeDoubleToInt(d));
    }

    public static char safeDoubleToChar(double d) {
        return SafeMath.safeIntToChar(safeDoubleToInt(d));
    }

    public static short safeDoubleToShort(double d) {
        return SafeMath.safeIntToShort(safeDoubleToInt(d));
    }

    public static int safeDoubleToInt(double d) {
        int i = (int) d;

        if (d != i) {
            throw new IllegalArgumentException(d + " can't be represented as int (out of range)");
        }

        return i;
    }

    public static long safeDoubleToLong(double d) {
        long l = (long) d;

        if (d != l) {
            throw new IllegalArgumentException(d + " can't be represented as int (out of range)");
        }

        return l;
    }

    public static byte highestOneBit(byte b) {
        return (byte) (b & (Integer.MIN_VALUE >>> numberOfLeadingZeros(b)));
    }

    public static char highestOneBit(char c) {
        return (char) (c & (Integer.MIN_VALUE >>> numberOfLeadingZeros(c)));
    }

    public static short highestOneBit(short s) {
        return (short) (s & (Integer.MIN_VALUE >>> numberOfLeadingZeros(s)));
    }

    public static byte lowestOneBit(byte i) {
        // HD, Section 2-1
        return (byte) (i & -i);
    }

    public static char lowestOneBit(char i) {
        // HD, Section 2-1
        return (char) (i & -i);
    }

    public static short lowestOneBit(short i) {
        // HD, Section 2-1
        return (short) (i & -i);
    }

    public static int numberOfLeadingZeros(byte b) {
        // HD, Count leading 0's
        if (b <= 0)
            return b == 0 ? 8 : 0;
        int n = 7;
        if (b >= 1 <<  4) { n -=  4; b >>>=  4; }
        if (b >= 1 <<  2) { n -=  2; b >>>=  2; }
        return n - (b >>> 1);
    }

    public static int numberOfLeadingZeros(char c) {
        return numberOfLeadingZeros((short) c);
    }

    public static int numberOfLeadingZeros(short s) {
        // HD, Count leading 0's
        if (s <= 0)
            return s == 0 ? 16 : 0;
        int n = 15;
        if (s >= 1 <<  8) { n -=  8; s >>>=  8; }
        if (s >= 1 <<  4) { n -=  4; s >>>=  4; }
        if (s >= 1 <<  2) { n -=  2; s >>>=  2; }
        return n - (s >>> 1);
    }

    public static int numberOfTrailingZeros(byte b) {
        // HD, Count trailing 0's
        b = (byte) (~b & (b - 1));
        if (b <= 0) return b & 8;
        int n = 1;
        if (b > 1 <<  4) { n +=  4; b >>>=  4; }
        if (b > 1 <<  2) { n +=  2; b >>>=  2; }
        return n + (b >>> 1);
    }

    public static int numberOfTrailingZeros(char c) {
        return numberOfTrailingZeros((short) c);
    }

    public static int numberOfTrailingZeros(short i) {
        // HD, Count trailing 0's
        i = (short) (~i & (i - 1));
        if (i <= 0) return i & 16;
        int n = 1;
        if (i > 1 <<  8) { n +=  8; i >>>=  8; }
        if (i > 1 <<  4) { n +=  4; i >>>=  4; }
        if (i > 1 <<  2) { n +=  2; i >>>=  2; }
        return n + (i >>> 1);
    }

    public static float floorDiv(float f, float divider) {
        float a = f / divider;
        float negativeBoundary = -1;
        float positiveBoundary = 0;
        float sign = -1;
        int exponent = Math.getExponent(a);

        if (exponent < 0) {
            /*
             * Absolute value of argument is less than 1.
             * floorOrceil(-0.0) => -0.0
             * floorOrceil(+0.0) => +0.0
             */
            return ((a == 0.0) ? a :
                    ( (a < 0.0) ?  negativeBoundary : positiveBoundary) );
        } else if (exponent >= 52) {
            /*
             * Infinity, NaN, or a value so large it must be integral.
             */
            return a;
        }

        int doppel = Float.floatToRawIntBits(a);
        int mask   = 0x007FFFFFFF >> exponent;

        if ( (mask & doppel) == 0L )
            return a; // integral value
        else {
            float result = Float.intBitsToFloat(doppel & (~mask));

            if (sign*a > 0.0)
                result = result + sign;

            return result;
        }
    }

    public static double floorDiv(double d, double divider) {
        return Math.floor(d / divider);
    }

    public static int squared(int d) {
        return d * d;
    }

    public static long squared(long d) {
        return d * d;
    }

    public static float squared(float d) {
        return d * d;
    }

    public static double squared(double d) {
        return d * d;
    }

    public static int cubed(int d) {
        return d * d * d;
    }

    public static long cubed(long d) {
        return d * d * d;
    }

    public static float cubed(float d) {
        return d * d * d;
    }

    public static double cubed(double d) {
        return d * d * d;
    }

    public static int positivePow(int base, @Range(from = 1, to = Integer.MAX_VALUE) final int power) {
        for (int i = 1; i < power; ++i) {
            base *= base;
        }

        return base;
    }

    public static long positivePow(long base, @Range(from = 1, to = Integer.MAX_VALUE) final int power) {
        for (int i = 1; i < power; ++i) {
            base *= base;
        }

        return base;
    }

    public static float positivePow(float base, @Range(from = 1, to = Integer.MAX_VALUE) final int power) {
        for (int i = 1; i < power; ++i) {
            base *= base;
        }

        return base;
    }

    public static double positivePow(double base, @Range(from = 1, to = Integer.MAX_VALUE) final int power) {
        for (int i = 1; i < power; ++i) {
            base *= base;
        }

        return base;
    }

    public static double pow(double base, final int power) {
        if (power == 0) {
            return base;
        } else if (power < 0) {
            return 1 / positivePow(base, -power);
        } else {
            return positivePow(base, power);
        }
    }

    public static long product(int... numbers) {
        long result = 1;

        for (int i : numbers) {
            result *= i;
        }

        return result;
    }

    public static int[] compressedIndexToMatrixIndexes(@Range(from = 0, to = Integer.MAX_VALUE) int index,
                                                       @Range(from = 1, to = Integer.MAX_VALUE) int... dimensions) {
        int[] result = new int[dimensions.length];

        for (int i = dimensions.length; i-- > 0;) {
            result[i] = Math.floorMod(index, dimensions[i]);
            index = Math.floorDiv(index, dimensions[i]);
        }

        return result;
    }

    /**
     * Incorporate a new doubles value using Kahan summation /
     * compensation summation.
     *
     * High-order bits of the sum are in intermediateSum[0], low-order
     * bits of the sum are in intermediateSum[1], any additional
     * elements are application-specific.
     *
     * @param intermediateSum the high-order and low-order words of the intermediate sum
     * @param value the name value to be included in the running sum
     */
    public static double[] sumWithCompensation(double[] intermediateSum, double value) {
        double tmp = value - intermediateSum[1];
        double sum = intermediateSum[0];
        double velvel = sum + tmp; // Little wolf of rounding error
        intermediateSum[1] = (velvel - sum) - tmp;
        intermediateSum[0] = velvel;
        return intermediateSum;
    }

    /**
     * If the compensated sum is spuriously NaN from accumulating one
     * or more same-signed infinite values, return the
     * correctly-signed infinity stored in the simple sum.
     */
    public static double computeFinalSum(double[] summands) {
        // Better error bounds to add both terms as the final sum
        double tmp = summands[0] + summands[1];
        double simpleSum = summands[summands.length - 1];
        if (Double.isNaN(tmp) && Double.isInfinite(simpleSum))
            return simpleSum;
        else
            return tmp;
    }

    public static int clamp(int n, int min, int max) {
        if (n < min) {
            return min;
        }

        return Math.min(n, max);
    }

    public static long clamp(long n, long min, long max) {
        if (n < min) {
            return min;
        }

        return Math.min(n, max);
    }

    public static float clamp(float n, float min, float max) {
        if (Float.isNaN(n))
            return Float.NaN; // return canonical NaN regardless of the input

        if (n == 0.0f) {
            if (Float.floatToRawIntBits(max) == NEGATIVE_ZERO_FLOAT_BITS) {
                return max;
            }

            if (Float.floatToRawIntBits(min) == NEGATIVE_ZERO_FLOAT_BITS) {
                return n;
            }
        }

        if (!(n <= max)) {
            return max;
        }

        return (n <= min) ? n : min;
    }

    public static double clamp(double n, double min, double max) {
        if (Double.isNaN(n))
            return Double.NaN; // return canonical NaN regardless of the input

        if (n == 0.0f) {
            if (Double.doubleToRawLongBits(max) == NEGATIVE_ZERO_DOUBLE_BITS) {
                return max;
            }

            if (Double.doubleToRawLongBits(min) == NEGATIVE_ZERO_DOUBLE_BITS) {
                return n;
            }
        }

        if (!(n <= max)) {
            return max;
        }

        return (n <= min) ? n : min;
    }
}
