package com.github.svegon.utils.math.geometry.surface;

import com.github.svegon.utils.annotations.TrustedMutableArg;
import com.github.svegon.utils.math.MathUtil;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.floats.FloatArrays;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class SurfaceUtil {
    private SurfaceUtil() {
        throw new UnsupportedOperationException();
    }

    public static double squaredDistance(double @NotNull @TrustedMutableArg [] first,
                                         double @NotNull @TrustedMutableArg [] second) {
        if (first.length != second.length) {
            throw new IllegalArgumentException("Vectors don't match in dimensions.");
        }

        int length = first.length;
        double result = 0;

        for (int i = 0; i < length; i++) {
            result += MathUtil.squared(first[i] - second[i]);
        }

        return result;
    }


    public static double distance(double @NotNull @TrustedMutableArg [] first,
                                  double @NotNull @TrustedMutableArg [] second) {
        return Math.sqrt(squaredDistance(first, second));
    }


    @Contract(value = "_ -> new", pure = true)
    public static float[] vectorSum(final float @NotNull [] @NotNull @TrustedMutableArg ... arrays) {
        if (arrays.length == 0) {
            return FloatArrays.EMPTY_ARRAY;
        }

        float[] result = arrays[0].clone();
        int arraysLength = arrays.length;
        int length = result.length;

        for (int i = 1; i < arraysLength; i++) {
            float[] a = arrays[i];

            for (int j = 0; j < length; j++) {
                result[j] += a[j];
            }
        }

        return result;
    }

    /**
     * @param first first surface
     * @param second second surface
     * @return a new array with each element result[i] == first[i] - second[i] for any ints i; i >= 0 && i < result.length
     */
    public static float[] vectorSubstract(float @NotNull @TrustedMutableArg [] first,
                                          float @NotNull @TrustedMutableArg [] second) {
        if (first.length != second.length) {
            throw new IllegalArgumentException("Vectors have different dimensions.");
        }

        float[] result = first.clone();
        int length = result.length;

        for (int i = 0; i < length; i++) {
            result[i] -= second[i];
        }

        return result;
    }

    @Contract(value = "_ -> new", pure = true)
    public static double[] vectorSum(final double @NotNull [] @NotNull @TrustedMutableArg ... arrays) {
        if (arrays.length == 0) {
            return DoubleArrays.EMPTY_ARRAY;
        }

        double[] result = arrays[0].clone();
        int arraysLength = arrays.length;
        int length = result.length;

        for (int i = 1; i < arraysLength; i++) {
            double[] a = arrays[i];

            for (int j = 0; j < length; j++) {
                result[j] += a[j];
            }
        }

        return result;
    }

    /**
     * @param first first surface
     * @param second second surface
     * @return a new array with each element result[i] == first[i] - second[i] for any ints i; i >= 0 && i < result.length
     */
    public static double[] vectorSubstract(double @NotNull @TrustedMutableArg [] first,
                                           double @NotNull @TrustedMutableArg [] second) {
        if (first.length != second.length) {
            throw new IllegalArgumentException("Vectors have different dimensions.");
        }

        double[] result = first.clone();
        int length = result.length;

        for (int i = 0; i < length; i++) {
            result[i] -= second[i];
        }

        return result;
    }
}
