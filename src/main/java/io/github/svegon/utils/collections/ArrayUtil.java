package io.github.svegon.utils.collections;

import io.github.svegon.utils.annotations.TrustedMutableArg;
import io.github.svegon.utils.collections.stream.*;
import io.github.svegon.utils.fast.util.booleans.ImprovedBooleanCollection;
import io.github.svegon.utils.fast.util.bytes.ImprovedByteCollection;
import io.github.svegon.utils.fast.util.chars.ImprovedCharCollection;
import io.github.svegon.utils.fast.util.floats.ImprovedFloatCollection;
import io.github.svegon.utils.fast.util.shorts.ImprovedShortCollection;
import io.github.svegon.utils.multithreading.ThreadUtil;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.Swapper;
import it.unimi.dsi.fastutil.booleans.*;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.chars.*;
import it.unimi.dsi.fastutil.doubles.*;
import it.unimi.dsi.fastutil.floats.*;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import it.unimi.dsi.fastutil.shorts.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;

/**
 * class providing methods for working with arrays
 *
 * warning:
 * Many of methods on this class would work way off expectations or even
 * throw an error if its parameters were modified during their running.
 *
 * @since 1.0.0
 */
public final class ArrayUtil {
    private ArrayUtil() {
        throw new AssertionError();
    }

    public static final int PARALLEL_REVERSE_NO_FORK = 8192;

    /**
     * Creates a new array with the same component type and length as {@param array}.
     * @param componentType the component type as class of the resulting array
     * @param length length of the returning array
     * @param <T> component of {@param array}
     * @return a new array instance such that
     * {@return}.getClass().getComponentType() == {@param componentType}
     * && {@return}.length == {@param length}
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(final @NotNull Class<T> componentType, int length) {
        return (T[]) Array.newInstance(componentType, length);
    }

    /**
     * Creates a new array with the same component type and length as {@param array}.
     * @param array the original array
     * @param length length of the returning array
     * @param <T> component of {@param array}
     * @return a new array instance such that
     * {@return}.getClass().getComponentType() == {@param array}.getClass().getComponentType()
     * && {@return}.length == {@param length}
     */
    public static <T> T[] newArray(final T @NotNull [] array, int length) {
        return com.google.common.collect.ObjectArrays.newArray(array, length);
    }

    /**
     * Creates a new array with the same component type and length as {@param array}.
     * @param array the original array
     * @param <T> component of {@param array}
     * @return a new array instance such that
     * {@return}.getClass().getComponentType() == {@param array}.getClass().getComponentType()
     * && {@return}.length == {@param array}.length
     */
    public static <T> T[] newArray(final T @NotNull [] array) {
        return newArray(array, array.length);
    }

    /**
     * @param array array of elements
     * @param newLength length of the return array
     * @param <T> component type of the given array
     * @return if newLength > array.length returns a new array
     *          with newLength - array.length elements appended to the beggining of the array;
     *          otherwise same return a new array with array.length - newLength elements
     *          removed from the beggining of the array
     * @throws NegativeArraySizeException if newLength < 0
     * @throws NullPointerException if array == null
     */
    @Contract(value = "_, _ -> new")
    public static <T> T[] copyOfTrimmedFromEnd(T @NotNull [] array, int newLength) {
        T[] newArray = newArray(array, newLength);

        if (newLength < array.length) {
            System.arraycopy(array, array.length - newLength, newArray, 0, newLength);
        } else  {
            System.arraycopy(array, 0, newArray, newLength - array.length, array.length);
        }

        return newArray;
    }

    /**
     * @param array array of elements
     * @param newLength length of the return array
     * @return if newLength > array.length returns a new array
     *          with newLength - array.length elements appended to the beggining of the array;
     *          otherwise same return a new array with array.length - newLength elements
     *          removed from the beggining of the array
     * @throws NegativeArraySizeException if newLength < 0
     * @throws NullPointerException if array == null
     */
    @Contract(value = "_, _ -> new")
    public static boolean[] copyOfTrimmedFromEnd(boolean @NotNull [] array, int newLength) {
        boolean[] newArray = new boolean[newLength];

        if (newLength < array.length) {
            System.arraycopy(array, array.length - newLength, newArray, 0, newLength);
        } else  {
            System.arraycopy(array, 0, newArray, newLength - array.length, array.length);
        }

        return newArray;
    }

    /**
     * @param array array of elements
     * @param newLength length of the return array
     * @return if newLength > array.length returns a new array
     *          with newLength - array.length elements appended to the beggining of the array;
     *          otherwise same return a new array with array.length - newLength elements
     *          removed from the beggining of the array
     * @throws NegativeArraySizeException if newLength < 0
     * @throws NullPointerException if array == null
     */
    @Contract(value = "_, _ -> new")
    public static byte[] copyOfTrimmedFromEnd(byte @NotNull [] array, int newLength) {
        byte[] newArray = new byte[newLength];

        if (newLength < array.length) {
            System.arraycopy(array, array.length - newLength, newArray, 0, newLength);
        } else  {
            System.arraycopy(array, 0, newArray, newLength - array.length, array.length);
        }

        return newArray;
    }

    /**
     * @param array array of elements
     * @param newLength length of the return array
     * @return if newLength > array.length returns a new array
     *          with newLength - array.length elements appended to the beggining of the array;
     *          otherwise same return a new array with array.length - newLength elements
     *          removed from the beggining of the array
     * @throws NegativeArraySizeException if newLength < 0
     * @throws NullPointerException if array == null
     */
    @Contract(value = "_, _ -> new")
    public static short[] copyOfTrimmedFromEnd(short @NotNull [] array, int newLength) {
        short[] newArray = new short[newLength];

        if (newLength < array.length) {
            System.arraycopy(array, array.length - newLength, newArray, 0, newLength);
        } else  {
            System.arraycopy(array, 0, newArray, newLength - array.length, array.length);
        }

        return newArray;
    }

    /**
     * @param array array of elements
     * @param newLength length of the return array
     * @return if newLength > array.length returns a new array
     *          with newLength - array.length elements appended to the beggining of the array;
     *          otherwise same return a new array with array.length - newLength elements
     *          removed from the beggining of the array
     * @throws NegativeArraySizeException if newLength < 0
     * @throws NullPointerException if array == null
     */
    @Contract(value = "_, _ -> new")
    public static int[] copyOfTrimmedFromEnd(int @NotNull [] array, int newLength) {
        int[] newArray = new int[newLength];

        if (newLength < array.length) {
            System.arraycopy(array, array.length - newLength, newArray, 0, newLength);
        } else  {
            System.arraycopy(array, 0, newArray, newLength - array.length, array.length);
        }

        return newArray;
    }

    /**
     * @param array array of elements
     * @param newLength length of the return array
     * @return if newLength > array.length returns a new array
     *          with newLength - array.length elements appended to the beggining of the array;
     *          otherwise same return a new array with array.length - newLength elements
     *          removed from the beggining of the array
     * @throws NegativeArraySizeException if newLength < 0
     * @throws NullPointerException if array == null
     */
    @Contract(value = "_, _ -> new")
    public static long[] copyOfTrimmedFromEnd(long @NotNull [] array, int newLength) {
        long[] newArray = new long[newLength];

        if (newLength < array.length) {
            System.arraycopy(array, array.length - newLength, newArray, 0, newLength);
        } else  {
            System.arraycopy(array, 0, newArray, newLength - array.length, array.length);
        }

        return newArray;
    }

    /**
     * @param array array of elements
     * @param newLength length of the return array
     * @return if newLength > array.length returns a new array
     *          with newLength - array.length elements appended to the beggining of the array;
     *          otherwise same return a new array with array.length - newLength elements
     *          removed from the beggining of the array
     * @throws NegativeArraySizeException if newLength < 0
     * @throws NullPointerException if array == null
     */
    @Contract(value = "_, _ -> new")
    public static char[] copyOfTrimmedFromEnd(char @NotNull [] array, int newLength) {
        char[] newArray = new char[newLength];

        if (newLength < array.length) {
            System.arraycopy(array, array.length - newLength, newArray, 0, newLength);
        } else  {
            System.arraycopy(array, 0, newArray, newLength - array.length, array.length);
        }

        return newArray;
    }

    /**
     * @param array array of elements
     * @param newLength length of the return array
     * @return if newLength > array.length returns a new array
     *          with newLength - array.length elements appended to the beggining of the array;
     *          otherwise same return a new array with array.length - newLength elements
     *          removed from the beggining of the array
     * @throws NegativeArraySizeException if newLength < 0
     * @throws NullPointerException if array == null
     */
    @Contract(value = "_, _ -> new")
    public static float[] copyOfTrimmedFromEnd(float @NotNull [] array, int newLength) {
        float[] newArray = new float[newLength];

        if (newLength < array.length) {
            System.arraycopy(array, array.length - newLength, newArray, 0, newLength);
        } else  {
            System.arraycopy(array, 0, newArray, newLength - array.length, array.length);
        }

        return newArray;
    }

    /**
     * @param array array of elements
     * @param newLength length of the return array
     * @return if newLength > array.length returns a new array
     *          with newLength - array.length elements appended to the beggining of the array;
     *          otherwise same return a new array with array.length - newLength elements
     *          removed from the beggining of the array
     * @throws NegativeArraySizeException if newLength < 0
     * @throws NullPointerException if array == null
     */
    @Contract(value = "_, _ -> new")
    public static double[] copyOfTrimmedFromEnd(double @NotNull [] array, int newLength) {
        double[] newArray = new double[newLength];

        if (newLength < array.length) {
            System.arraycopy(array, array.length - newLength, newArray, 0, newLength);
        } else  {
            System.arraycopy(array, 0, newArray, newLength - array.length, array.length);
        }

        return newArray;
    }

    /**
     * @param first first array to be merged
     * @param second second part of the merged array
     * @param <E> component type
     * @return new array including elements of both given array
     * @throws NullPointerException if first == null; if second == null
     */
    @Contract(value = "_, _ -> new")
    @SafeVarargs
    public static <E> E[] concat(E @NotNull [] first, E @NotNull ... second) {
        E[] newArray = Arrays.copyOf(first, first.length + second.length);

        System.arraycopy(second, 0, newArray, first.length, second.length);

        return newArray;
    }

    /**
     * @param arrays array of arrays to be all merged into 1 array
     * @param <E> component type of each array
     * @return a new array containing all elements of each array in array in their order in arrays;
     *          e.g. merge(new String[]{"a", "b", c"}, new String[]{"d", e", f"}) ->
     *          String[]{"a", "b", c", "d", e", f"}
     * @throws NullPointerException if arrays is null or if any of its elements is null
     */
    @Contract(value = "_ -> new")
    @SuppressWarnings("unchecked")
    public static <E> E[] concat(E @NotNull [] @NotNull ... arrays) {
        E[] newArray = newArray((Class<E>) arrays.getClass().getComponentType().getComponentType(),
                Arrays.stream(arrays).mapToInt((a) -> a.length).sum());
        int offset = 0;

        for (E[] array : arrays) {
            System.arraycopy(array, 0, newArray, offset, array.length);
            offset += array.length;
        }

        return newArray;
    }

    /**
     * @param first first array to be merged
     * @param second second part of the merged array
     * @return new array including elements of both given array
     * @throws NullPointerException if first == null; if second == null
     */
    @Contract(value = "_, _ -> new")
    public static boolean[] concat(boolean @NotNull [] first, boolean @NotNull ... second) {
        boolean[] newArray = Arrays.copyOf(first, first.length + second.length);

        System.arraycopy(second, 0, newArray, first.length, second.length);

        return newArray;
    }

    /**
     * @param arrays array of arrays to be all merged into 1 array
     * @return a new array containing all elements of each array in array in their order in arrays;
     *          e.g. merge(new booleans[]{true, false, true}, new booleans[]{false, false}) ->
     *          booleans[]{true, false, true, false, false}
     * @throws NullPointerException if arrays is null or if any of its elements is null
     */
    @Contract(value = "_ -> new")
    public static boolean[] concat(boolean @NotNull [] @NotNull ... arrays) {
        boolean[] newArray = new boolean[Arrays.stream(arrays).mapToInt((a) -> a.length).sum()];
        int offset = 0;

        for (boolean[] array : arrays) {
            System.arraycopy(array, 0, newArray, offset, array.length);
            offset += array.length;
        }

        return newArray;
    }

    /**
     * @param first first array to be merged
     * @param second second part of the merged array
     * @return new array including elements of both given array
     * @throws NullPointerException if first == null; if second == null
     */
    @Contract(value = "_, _ -> new")
    public static byte[] concat(byte @NotNull [] first, byte @NotNull ... second) {
        byte[] newArray = Arrays.copyOf(first, first.length + second.length);

        System.arraycopy(second, 0, newArray, first.length, second.length);

        return newArray;
    }

    /**
     * @param arrays array of arrays to be all merged into 1 array
     * @return a new array containing all elements of each array in array in their order in arrays;
     *          e.g. merge(new bytes[]{1, 2, 3}, new bytes[]{0, 0}) ->
     *          bytes[]{1, 2, 3, 0, 0}
     * @throws NullPointerException if arrays is null or if any of ots elements is null
     */
    @Contract(value = "_ -> new")
    public static byte[] concat(byte @NotNull [] @NotNull ... arrays) {
        byte[] newArray = new byte[Arrays.stream(arrays).mapToInt((a) -> a.length).sum()];
        int offset = 0;

        for (byte[] array : arrays) {
            System.arraycopy(array, 0, newArray, offset, array.length);
            offset += array.length;
        }

        return newArray;
    }

    /**
     * @param first first array to be merged
     * @param second second part of the merged array
     * @return new array including elements of both given array
     * @throws NullPointerException if first == null; if second == null
     */
    @Contract(value = "_, _ -> new")
    public static short[] concat(short @NotNull [] first, short @NotNull ... second) {
        short[] newArray = Arrays.copyOf(first, first.length + second.length);

        System.arraycopy(second, 0, newArray, first.length, second.length);

        return newArray;
    }

    /**
     * @param arrays array of arrays to be all merged into 1 array
     * @return a new array containing all elements of each array in array in their order in arrays;
     *          e.g. merge(new short[]{1, 2, 3}, new short[]{0, 0}) ->
     *          short[]{1, 2, 3, 0, 0}
     * @throws NullPointerException if arrays is null or if any of ots elements is null
     */
    @Contract(value = "_ -> new")
    public static short[] concat(short @NotNull [] @NotNull ... arrays) {
        short[] newArray = new short[Arrays.stream(arrays).mapToInt((a) -> a.length).sum()];
        int offset = 0;

        for (short[] array : arrays) {
            System.arraycopy(array, 0, newArray, offset, array.length);
            offset += array.length;
        }

        return newArray;
    }

    /**
     * @param first first array to be merged
     * @param second second part of the merged array
     * @return new array including elements of both given array
     * @throws NullPointerException if first == null; if second == null
     */
    @Contract(value = "_, _ -> new")
    public static int[] concat(int @NotNull [] first, int @NotNull ... second) {
        int[] newArray = Arrays.copyOf(first, first.length + second.length);

        System.arraycopy(second, 0, newArray, first.length, second.length);

        return newArray;
    }

    /**
     * @param arrays array of arrays to be all merged into 1 array
     * @return a new array containing all elements of each array in array in their order in arrays;
     *          e.g. merge(new ints[]{1, 2, 3}, new ints[]{0, 0}) ->
     *          ints[]{1, 2, 3, 0, 0}
     * @throws NullPointerException if arrays is null or if any of ots elements is null
     */
    @Contract(value = "_ -> new")
    public static int[] concat(int @NotNull [] @NotNull ... arrays) {
        int[] newArray = new int[Arrays.stream(arrays).mapToInt((a) -> a.length).sum()];
        int offset = 0;

        for (int[] array : arrays) {
            System.arraycopy(array, 0, newArray, offset, array.length);
            offset += array.length;
        }

        return newArray;
    }

    /**
     * @param first first array to be merged
     * @param second second part of the merged array
     * @return new array including elements of both given array
     * @throws NullPointerException if first == null; if second == null
     */
    @Contract(value = "_, _ -> new")
    public static long[] concat(long @NotNull [] first, long @NotNull ... second) {
        long[] newArray = Arrays.copyOf(first, first.length + second.length);

        System.arraycopy(second, 0, newArray, first.length, second.length);

        return newArray;
    }

    /**
     * @param arrays array of arrays to be all merged into 1 array
     * @return a new array containing all elements of each array in array in their order in arrays;
     *          e.g. merge(new longs[]{1, 2, 3}, new longs[]{0, 0}) ->
     *          longs[]{1, 2, 3, 0, 0}
     * @throws NullPointerException if arrays is null or if any of ots elements is null
     */
    @Contract(value = "_ -> new")
    public static long[] concat(long @NotNull [] @NotNull ... arrays) {
        long[] newArray = new long[Arrays.stream(arrays).mapToInt((a) -> a.length).sum()];
        int offset = 0;

        for (long[] array : arrays) {
            System.arraycopy(array, 0, newArray, offset, array.length);
            offset += array.length;
        }

        return newArray;
    }

    /**
     * @param first first array to be merged
     * @param second second part of the merged array
     * @return new array including elements of both given array
     * @throws NullPointerException if first == null; if second == null
     */
    @Contract(value = "_, _ -> new")
    public static float[] concat(float @NotNull [] first, float @NotNull ... second) {
        float[] newArray = Arrays.copyOf(first, first.length + second.length);

        System.arraycopy(second, 0, newArray, first.length, second.length);

        return newArray;
    }

    /**
     * @param arrays array of arrays to be all merged into 1 array
     * @return a new array containing all elements of each array in array in their order in arrays;
     *          e.g. merge(new float[]{0.4F, 0.5F, -3F}, new float[]{0F, Float.NaN}) ->
     *          float[]{0.4F, 0.5F, -3.0F, 0.0F, NaN}
     * @throws NullPointerException if arrays is null or if any of ots elements is null
     */
    @Contract(value = "_ -> new")
    public static float[] concat(float @NotNull [] @NotNull ... arrays) {
        float[] newArray = new float[Arrays.stream(arrays).mapToInt((a) -> a.length).sum()];
        int offset = 0;

        for (float[] array : arrays) {
            System.arraycopy(array, 0, newArray, offset, array.length);
            offset += array.length;
        }

        return newArray;
    }

    /**
     * @param first first array to be merged
     * @param second second part of the merged array
     * @return new array including elements of both given array
     * @throws NullPointerException if first == null; if second == null
     */
    @Contract(value = "_, _ -> new")
    public static double[] concat(double @NotNull [] first, double @NotNull ... second) {
        double[] newArray = Arrays.copyOf(first, first.length + second.length);

        System.arraycopy(second, 0, newArray, first.length, second.length);

        return newArray;
    }

    /**
     * @param arrays array of arrays to be all merged into 1 array
     * @return a new array containing all elements of each array in array in their order in arrays;
     *          e.g. merge(new doubles[]{0.4F, 0.5F, -3F}, new doubles[]{0F, Double.NaN}) ->
     *          doubles[]{0.4F, 0.5F, -3.0F, 0.0F, NaN}
     * @throws NullPointerException if arrays is null or if any of ots elements is null
     */
    @Contract(value = "_ -> new")
    public static double[] concat(double @NotNull [] @NotNull ... arrays) {
        double[] newArray = new double[Arrays.stream(arrays).mapToInt((a) -> a.length).sum()];
        int offset = 0;

        for (double[] array : arrays) {
            System.arraycopy(array, 0, newArray, offset, array.length);
            offset += array.length;
        }

        return newArray;
    }

    /**
     * Creates a new array with all elements at indexes marked with {@param indexesToRemove} as true
     * @param array the base array
     * @param indexesToRemove a BitSet determining which elements to remove
     * @param <E> component type of the arrays
     * @return a new array with length {@param array}.length - {@param indexesToRemove}.cardinality()
     * and containing all elements of {@param array} except those at indexes such that an index i
     * {@param indexesToRemove}.get(i) in the same order
     */
    @Contract(value = "_, _ -> new")
    public static <E> E[] filter(@TrustedMutableArg E @NotNull [] array,
                                 @TrustedMutableArg @NotNull BitSet indexesToRemove) {
        E[] newArray = newArray(array, array.length - indexesToRemove.cardinality());
        int filledSlots = 0;
        int startingIndexOfRange = 0;
        int endingIndexOfRange = 0;

        if (newArray.length == 0) {
            return newArray;
        }

        while ((endingIndexOfRange = indexesToRemove.nextSetBit(startingIndexOfRange)) >= 0
                && endingIndexOfRange <= array.length) {
            int length = endingIndexOfRange - startingIndexOfRange;

            System.arraycopy(array, startingIndexOfRange, newArray, filledSlots, length);

            filledSlots += length;
            startingIndexOfRange = endingIndexOfRange + 1;
        }

        System.arraycopy(array, startingIndexOfRange, newArray, filledSlots, newArray.length - filledSlots);

        return newArray;
    }

    /**
     * Creates a new array with elements not matching the {@param filter} removed.
     * @param array the base array
     * @param filter the predicate determining which elements to remove
     * @param <E> component type of the base and return array
     * @return an array which contains all elements such that
     * each element e returns {@code true} when calling {@param filter}.test(e)
     */
    @Contract(value = "_, _ -> new")
    public static <E> E[] filter(@TrustedMutableArg E @NotNull [] array, @NotNull Predicate<? super E> filter) {
        BitSet indexesToRemove = new BitSet(array.length);

        for (int i = 0; i < array.length; ++i) {
            if (!filter.test(array[i])) {
                indexesToRemove.set(i);
            }
        }

        return filter(array, indexesToRemove);
    }

    /**
     * Creates a new array with the elements at {@param index remove}
     * @param array the original array
     * @param index the index of the element to be removed
     * @param <E> component type of {@param array}
     * @return a new array with length {@param array}.length - 1
     * and all elements from {@param index} shifted 1 index to the left
     */
    @Contract(value = "_, _ -> new")
    public static <E> E @NotNull [] pop(E @NotNull [] array, int index) {
        E[] newArray = newArray(array, array.length - 1);

        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + 1, newArray, index, array.length - index - 1);

        return newArray;
    }

    @Contract(value = "_, _, _ -> param1")
    public static <E> E[] clear(E @NotNull [] a, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = null;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - from - i);
        return a;
    }

    @Contract(value = "_ -> param1")
    public static <E> E[] clear(E @NotNull [] a) {
        return clear(a, 0, a.length);
    }

    @Contract(value = "_, _, _ -> param1")
    public static boolean[] clear(boolean @NotNull [] a, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = false;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    @Contract(value = "_ -> param1")
    public static boolean[] clear(boolean @NotNull [] a) {
        return clear(a, 0, a.length);
    }

    @Contract(value = "_, _, _ -> param1")
    public static byte[] clear(byte @NotNull [] a, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = 0;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    @Contract(value = "_ -> param1")
    public static byte[] clear(byte @NotNull [] a) {
        return clear(a, 0, a.length);
    }

    @Contract(value = "_, _, _ -> param1")
    public static short[] clear(short @NotNull [] a, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = 0;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    @Contract(value = "_ -> param1")
    public static short[] clear(short @NotNull [] a) {
        return clear(a, 0, a.length);
    }

    @Contract(value = "_, _, _ -> param1")
    public static int[] clear(int @NotNull [] a, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = 0;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    @Contract(value = "_ -> param1")
    public static int[] clear(int @NotNull [] a) {
        return clear(a, 0, a.length);
    }

    @Contract(value = "_, _, _ -> param1")
    public static long[] clear(long @NotNull [] a, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = 0;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    @Contract(value = "_ -> param1")
    public static long[] clear(long @NotNull [] a) {
        return clear(a, 0, a.length);
    }

    @Contract(value = "_, _, _ -> param1")
    public static char[] clear(char @NotNull [] a, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = 0;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    @Contract(value = "_ -> param1")
    public static char[] clear(char @NotNull [] a) {
        return clear(a, 0, a.length);
    }

    @Contract(value = "_, _, _ -> param1")
    public static float[] clear(float @NotNull [] a, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = 0;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    @Contract(value = "_ -> param1")
    public static float[] clear(float @NotNull [] a) {
        return clear(a, 0, a.length);
    }

    @Contract(value = "_, _, _ -> param1")
    public static double[] clear(double @NotNull [] a, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = 0;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    public static double[] clear(double @NotNull [] a) {
        return clear(a, 0, a.length);
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param value value of every element after initializing
     * @param <E> component type of the given array
     * @return the array after initializing
     *
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> param1")
    public static <E> E[] fill(@TrustedMutableArg(isMutated = true) E @NotNull [] a, @Nullable E value) {
        if (value == null) {
            return clear(a);
        }

        a[0] = value;
        int i = 1;
        int half = a.length >>> 1;

        while (i <= half) {
            System.arraycopy(a, 0, a, i, i);
            i <<= 1;
        }

        System.arraycopy(a, 0, a, i, a.length - i);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param from the first index to be initialized (inclusive)
     * @param to the last index to be initialized (exclusive)
     * @param value value of every element after initializing
     * @param <E> component type of the given array
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> param1")
    public static <E> E[] fill(@TrustedMutableArg(isMutated = true) E @NotNull [] a, int from, int to,
                               @Nullable E value) {
        if (value == null) {
            return clear(a, from, to);
        }

        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = value;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param value value of every element after initializing
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> param1")
    public static boolean[] fill(@TrustedMutableArg(isMutated = true) boolean @NotNull [] a, boolean value) {
        return fill(a, 0, a.length, value);
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param from the first index to be initialized (inclusive)
     * @param to the last index to be initialized (exclusive)
     * @param value value of every element after initializing
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> param1")
    public static boolean[] fill(@TrustedMutableArg(isMutated = true) boolean @NotNull [] a, int from, int to,
                                 boolean value) {
        if (!value) {
            return clear(a, from, to);
        }

        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = true;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param value value of every element after initializing
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> param1")
    public static byte[] fill(byte @NotNull [] a, byte value) {
        return fill(a, 0, a.length, value);
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param from the first index to be initialized (inclusive)
     * @param to the last index to be initialized (exclusive)
     * @param value value of every element after initializing
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> param1")
    public static byte[] fill(byte @NotNull @TrustedMutableArg(isMutated = true) [] a, int from, int to, byte value) {
        if (value == 0) {
            return clear(a, from, to);
        }

        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = value;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param value value of every element after initializing
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> param1")
    public static short[] fill(short @NotNull @TrustedMutableArg(isMutated = true) [] a, short value) {
        if (value == 0) {
            return clear(a);
        }

        a[0] = value;
        int i = 1;
        int half = a.length >>> 1;

        while (i <= half) {
            System.arraycopy(a, 0, a, i, i);
            i <<= 1;
        }

        System.arraycopy(a, 0, a, i, a.length - i);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param from the first index to be initialized (inclusive)
     * @param to the last index to be initialized (exclusive)
     * @param value value of every element after initializing
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> param1")
    public static short[] fill(short @NotNull @TrustedMutableArg(isMutated = true) [] a, int from, int to, short value) {
        if (value == 0) {
            return clear(a, from, to);
        }

        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = value;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param value value of every element after initializing
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> param1")
    public static int[] fill(int @NotNull @TrustedMutableArg(isMutated = true) [] a, int value) {
        if (value == 0) {
            return clear(a);
        }

        a[0] = value;
        int i = 1;
        int half = a.length >>> 1;

        while (i <= half) {
            System.arraycopy(a, 0, a, i, i);
            i <<= 1;
        }

        System.arraycopy(a, 0, a, i, a.length - i);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param from the first index to be initialized (inclusive)
     * @param to the last index to be initialized (exclusive)
     * @param value value of every element after initializing
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> param1")
    public static int[] fill(int @NotNull @TrustedMutableArg(isMutated = true) [] a, int from, int to, int value) {
        if (value == 0) {
            return clear(a, from, to);
        }

        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = value;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param value value of every element after initializing
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> param1")
    public static long[] fill(long @NotNull @TrustedMutableArg(isMutated = true) [] a, long value) {
        if (value == 0) {
            return clear(a);
        }

        a[0] = value;
        int i = 1;
        int half = a.length >>> 1;

        while (i <= half) {
            System.arraycopy(a, 0, a, i, i);
            i <<= 1;
        }

        System.arraycopy(a, 0, a, i, a.length - i);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param from the first index to be initialized (inclusive)
     * @param to the last index to be initialized (exclusive)
     * @param value value of every element after initializing
     * @return the array after initializing
     *
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> param1")
    public static long[] fill(long @TrustedMutableArg(isMutated = true) @NotNull [] a, int from, int to, long value) {
        if (value == 0) {
            return clear(a, from, to);
        }

        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = value;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param value value of every element after initializing
     * @return the array after initializing
     *
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> param1")
    public static char[] fill(char @TrustedMutableArg(isMutated = true) @NotNull [] a, char value) {
        if (value == 0) {
            return clear(a);
        }

        a[0] = value;
        int i = 1;
        int half = a.length >>> 1;

        while (i <= half) {
            System.arraycopy(a, 0, a, i, i);
            i <<= 1;
        }

        System.arraycopy(a, 0, a, i, a.length - i);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param from the first index to be initialized (inclusive)
     * @param to the last index to be initialized (exclusive)
     * @param value value of every element after initializing
     * @return the array after initializing
     *
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> param1")
    public static char[] fill(char @NotNull @TrustedMutableArg(isMutated = true) [] a, int from, int to, char value) {
        if (value == 0) {
            return clear(a, from, to);
        }

        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = value;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param value value of every element after initializing
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> param1")
    public static float[] fill(float @TrustedMutableArg(isMutated = true) @NotNull [] a, float value) {
        if (Float.floatToRawIntBits(value) == 0) {
            return clear(a);
        }

        a[0] = value;
        int i = 1;
        int half = a.length >>> 1;

        while (i <= half) {
            System.arraycopy(a, 0, a, i, i);
            i <<= 1;
        }

        System.arraycopy(a, 0, a, i, a.length - i);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param from the first index to be initialized (inclusive)
     * @param to the last index to be initialized (exclusive)
     * @param value value of every element after initializing
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> param1")
    public static float[] fill(float @NotNull @TrustedMutableArg(isMutated = true) [] a, int from, int to,
                               float value) {
        if (Float.floatToRawIntBits(value) == 0) {
            return clear(a, from, to);
        }

        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = value;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing otherwise the results won't be reliable.
     *
     * @param a array to be initialized
     * @param value value of every element after initializing
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> param1")
    public static double[] fill(double @NotNull @TrustedMutableArg(isMutated = true) [] a, double value) {
        if (Double.doubleToRawLongBits(value) == 0) {
            return clear(a);
        }

        a[0] = value;
        int i = 1;
        int half = a.length >>> 1;

        while (i <= half) {
            System.arraycopy(a, 0, a, i, i);
            i <<= 1;
        }

        System.arraycopy(a, 0, a, i, a.length - i);
        return a;
    }

    /**
     * Set all elements of the specified array to the specified element.
     *
     * The effect is equal to using {@code Arrays.fill}
     * but this implementation uses {@code System.arraycopy} to perform faster.
     * This also means the array can't be accessed by external threads
     * while initializing to ensure correct results.
     *
     * @param a array to be initialized
     * @param from the first index to be initialized (inclusive)
     * @param to the last index to be initialized (exclusive)
     * @param value value of every element after initializing
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> param1")
    public static double[] fill(double @TrustedMutableArg(isMutated = true) @NotNull [] a, int from, int to,
                                double value) {
        if (Double.doubleToRawLongBits(value) == 0) {
            return clear(a, from, to);
        }

        Preconditions.checkPositionIndexes(from, to, a.length);

        a[from] = value;
        int i = 1;
        int half = (to - from) >>> 1;

        while (i <= half) {
            System.arraycopy(a, from, a, from + i, i);
            i <<= 1;
        }

        System.arraycopy(a, from, a,from + i, to - i - from);
        return a;
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> param1")
    public static <T> T[] setAll(T @NotNull [] a, @NotNull IntFunction<? extends T> mapper) {
        return setRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> param1")
    public static <T> T[] setRange(T @NotNull [] a, @NotNull IntFunction<? extends T> mapper, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        while (to-- != from) {
            a[to] = mapper.apply(to);
        }

        return a;
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> param1")
    public static boolean[] setAll(boolean @NotNull [] a, @NotNull IntPredicate mapper) {
        return setRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> param1")
    public static boolean[] setRange(boolean @NotNull [] a, @NotNull IntPredicate mapper, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        while (to-- != from) {
            a[to] = mapper.test(to);
        }

        return a;
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> param1")
    public static byte[] setAll(byte @NotNull [] a, @NotNull Int2ByteFunction mapper) {
        return setRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> param1")
    public static byte[] setRange(byte @NotNull [] a, @NotNull Int2ByteFunction mapper, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        while (to-- > from) {
            a[to] = mapper.apply(to);
        }

        return a;
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract("_, _ -> param1")
    public static char[] setAll(char @NotNull [] a, @NotNull Int2CharFunction mapper) {
        return setRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract("_, _, _, _ -> param1")
    public static char[] setRange(char @NotNull [] a, Int2CharFunction mapper, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        while (to-- > from) {
            a[to] = mapper.apply(to);
        }

        return a;
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> param1")
    public static short[] setAll(short @NotNull [] a, Int2ShortFunction mapper) {
        return setRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract("_, _, _, _ -> param1")
    public static short[] setRange(short @NotNull [] a, @NotNull Int2ShortFunction mapper, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        while (to-- > from) {
            a[to] = mapper.apply(to);
        }

        return a;
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract("_, _ -> param1")
    public static int[] setAll(int @NotNull [] a, @NotNull IntUnaryOperator mapper) {
        return setRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract("_, _, _, _ -> param1")
    public static int[] setRange(int @NotNull [] a, IntUnaryOperator mapper, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        while (to-- > from) {
            a[to] = mapper.applyAsInt(to);
        }

        return a;
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract("_, _ -> param1")
    public static long[] setAll(long @NotNull [] a, Int2LongFunction mapper) {
        return setRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract("_, _, _, _ -> param1")
    public static long[] setRange(long @NotNull [] a, Int2LongFunction mapper, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        while (to-- > from) {
            a[to] = mapper.apply(to);
        }

        return a;
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract("_, _ -> param1")
    public static float[] setAll(float @NotNull [] a, Int2FloatFunction mapper) {
        return setRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract("_, _, _, _ -> param1")
    public static float[] setRange(float @NotNull [] a, Int2FloatFunction mapper, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        while (to-- > from) {
            a[to] = mapper.apply(to);
        }

        return a;
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract("_, _ -> param1")
    public static double[] setAll(double @NotNull [] a, Int2DoubleFunction mapper) {
        return setRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the array after initializing
     * @since 1.0.0
     */
    @Contract("_, _, _, _ -> param1")
    public static double[] setRange(double @NotNull [] a, Int2DoubleFunction mapper, int from, int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);

        while (to-- > from) {
            a[to] = mapper.apply(to);
        }

        return a;
    }

    /**
     * Sets all elements in the specified range by applying each index to the updater method.
     * If the generator function throws an exception, it is relayed to the caller leaving the updated objects
     * in an undetermined state.
     *
     * @param updater a function accepting an index
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the array after initializing
     * @since 1.0.0
     */
    public static <T> ForkJoinArrayUpdateTask parallelSetRange(final @NotNull IntConsumer updater, int from, int to) {
        return (ForkJoinArrayUpdateTask) ThreadUtil.getPool().submit(new ForkJoinArrayUpdateTask(updater, from, to));
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    public static <T> ForkJoinArrayUpdateTask parallelSetAll(T @NotNull [] a, @NotNull IntFunction<? extends T> mapper) {
        return parallelSetRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     *
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the task performing the array updates
     * @since 1.0.0
     */
    public static <T> ForkJoinArrayUpdateTask parallelSetRange(final T @NotNull [] a,
                                                               final @NotNull IntFunction<? extends T> mapper,
                                                               final int from, final int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);
        Preconditions.checkNotNull(mapper);

        return parallelSetRange((i) -> a[i] = mapper.apply(i), from, to);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    public static ForkJoinArrayUpdateTask parallelSetAll(boolean @NotNull [] a, @NotNull IntPredicate mapper) {
        return parallelSetRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     *
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the task performing the array updates
     * @since 1.0.0
     */
    public static ForkJoinArrayUpdateTask parallelSetRange(final boolean @NotNull [] a,
                                                           final @NotNull IntPredicate mapper,
                                                           final int from, final int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);
        Preconditions.checkNotNull(mapper);

        return parallelSetRange((i) -> a[i] = mapper.test(i), from, to);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    public static ForkJoinArrayUpdateTask parallelSetAll(byte @NotNull [] a, @NotNull Int2ByteFunction mapper) {
        return parallelSetRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     *
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the task performing the array updates
     * @since 1.0.0
     */
    public static ForkJoinArrayUpdateTask parallelSetRange(final byte @NotNull [] a,
                                                           final @NotNull Int2ByteFunction mapper,
                                                           final int from, final int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);
        Preconditions.checkNotNull(mapper);

        return parallelSetRange((i) -> a[i] = mapper.get(i), from, to);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    public static ForkJoinArrayUpdateTask parallelSetAll(char @NotNull [] a, @NotNull Int2CharFunction mapper) {
        return parallelSetRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     *
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the task performing the array updates
     * @since 1.0.0
     */
    public static ForkJoinArrayUpdateTask parallelSetRange(final char @NotNull [] a,
                                                           final @NotNull Int2CharFunction mapper,
                                                           final int from, final int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);
        Preconditions.checkNotNull(mapper);

        return parallelSetRange((i) -> a[i] = mapper.get(i), from, to);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    public static ForkJoinArrayUpdateTask parallelSetAll(short @NotNull [] a, @NotNull Int2ShortFunction mapper) {
        return parallelSetRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     *
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the task performing the array updates
     * @since 1.0.0
     */
    public static ForkJoinArrayUpdateTask parallelSetRange(final short @NotNull [] a,
                                                           final @NotNull Int2ShortFunction mapper,
                                                           final int from, final int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);
        Preconditions.checkNotNull(mapper);

        return parallelSetRange((i) -> a[i] = mapper.get(i), from, to);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    public static ForkJoinArrayUpdateTask parallelSetAll(int @NotNull [] a, @NotNull IntUnaryOperator mapper) {
        return parallelSetRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     *
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the task performing the array updates
     * @since 1.0.0
     */
    public static ForkJoinArrayUpdateTask parallelSetRange(final int @NotNull [] a,
                                                           final @NotNull IntUnaryOperator mapper,
                                                           final int from, final int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);
        Preconditions.checkNotNull(mapper);

        return parallelSetRange((i) -> a[i] = mapper.applyAsInt(i), from, to);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @return the array after initializing
     * @since 1.0.0
     */
    public static ForkJoinArrayUpdateTask parallelSetAll(long @NotNull [] a, @NotNull Int2LongFunction mapper) {
        return parallelSetRange(a, mapper, 0, a.length);
    }

    /**
     * Set all elements of the specified array, using the provided generator function to compute each element.
     * If the generator function throws an exception, it is relayed to the caller
     * and the array is left in an indeterminate state.
     *
     * @param a array to be initialized
     * @param mapper a function accepting an index and producing the desired value for that position
     * @param from starting index of the range to be modified (inclusive)
     * @param to ending index of the range to be modified (exclusive)
     * @return the task performing the array updates
     * @since 1.0.0
     */
    public static ForkJoinArrayUpdateTask parallelSetRange(final long @NotNull [] a,
                                                           final @NotNull Int2LongFunction mapper,
                                                           final int from, final int to) {
        Preconditions.checkPositionIndexes(from, to, a.length);
        Preconditions.checkNotNull(mapper);

        return parallelSetRange((i) -> a[i] = mapper.get(i), from, to);
    }

    public static void reverse(final @NotNull Swapper swapper, int start, int end, int fence) {
        Preconditions.checkPositionIndexes(start, end, fence);

        int length = end - start;
        int center = length >>> 1 - start;

        if ((length & 1) == 0) {
            center++;
        }

        for (int i = 0; i < center; i++) {
            swapper.swap(start + i, fence - i);
        }
    }

    /**
     * swaps each element on index i with an element end - i for every integer start <= i < end
     * @param swapper the swapper used to swap the elements
     * @param start the first index to be swapped
     * @param end the last index + 1 to be swapped
     */
    public static void reverse(final @NotNull Swapper swapper, int start, int end) {
        reverse(swapper, start, end, end);
    }

    /**
     * performs the same as {@link #reverse(Swapper, int, int)} but in parallel if time effective
     * @param swapper the swapper used to swap the elements
     * @param start the first index to be swapped
     * @param end the last index + 1 to be swapped
     * @return the {@link ForkJoinTask} performing the reversion
     * @see #reverse(Swapper, int, int)
     */
    public static ForkJoinTask<Void> parallelReverse(final @NotNull Swapper swapper, int start, int end) {
        Preconditions.checkNotNull(swapper);

        ForkJoinPool pool = ThreadUtil.getPool();

        return pool.submit(new ForkJoinArrayReverseTask(start, end, end, swapper));
    }

    /**
     * Performs reversion in parallel in the same way as {@link #reverse(Swapper, int, int)}
     * @param array the array whose elements will be reversed
     * @param start the first index to be swapped
     * @param end the last index + 1 to be swapped
     * @param <E> component type of {@param array}
     * @return the {@link ForkJoinTask} performing the reversion
     * @see #parallelReverse(Swapper, int, int)
     */
    public static <E> ForkJoinTask<Void> parallelReverse(final E @NotNull [] array, int start, int end) {
        Preconditions.checkPositionIndexes(start, end, array.length);

        return parallelReverse((i, j) -> ObjectArrays.swap(array, i, j), start, end);
    }

    public static <E> ForkJoinTask<Void> parallelReverse(final E @NotNull [] array) {
        return parallelReverse(array, 0, array.length);
    }

    /**
     * Performs reversion in parallel in the same way as {@link #reverse(Swapper, int, int)}
     * @param array the array whose elements will be reversed
     * @param start the first index to be swapped
     * @param end the last index + 1 to be swapped
     * @return the {@link ForkJoinTask} performing the reversion
     * @see #parallelReverse(Swapper, int, int)
     */
    public static ForkJoinTask<Void> parallelReverse(final boolean @NotNull [] array, int start, int end) {
        Preconditions.checkPositionIndexes(start, end, array.length);

        return parallelReverse((i, j) -> BooleanArrays.swap(array, i, j), start, end);
    }

    public static ForkJoinTask<Void> parallelReverse(final boolean @NotNull [] array) {
        return parallelReverse(array, 0, array.length);
    }

    /**
     * Performs reversion in parallel in the same way as {@link #reverse(Swapper, int, int)}
     * @param array the array whose elements will be reversed
     * @param start the first index to be swapped
     * @param end the last index + 1 to be swapped
     * @return the {@link ForkJoinTask} performing the reversion
     * @see #parallelReverse(Swapper, int, int)
     */
    public static ForkJoinTask<Void> parallelReverse(final byte @NotNull [] array, int start, int end) {
        Preconditions.checkPositionIndexes(start, end, array.length);

        return parallelReverse((i, j) -> ByteArrays.swap(array, i, j), start, end);
    }

    public static ForkJoinTask<Void> parallelReverse(final byte @NotNull [] array) {
        return parallelReverse(array, 0, array.length);
    }

    /**
     * Performs reversion in parallel in the same way as {@link #reverse(Swapper, int, int)}
     * @param array the array whose elements will be reversed
     * @param start the first index to be swapped
     * @param end the last index + 1 to be swapped
     * @return the {@link ForkJoinTask} performing the reversion
     * @see #parallelReverse(Swapper, int, int)
     */
    public static ForkJoinTask<Void> parallelReverse(final char @NotNull [] array, int start, int end) {
        Preconditions.checkPositionIndexes(start, end, array.length);

        return parallelReverse((i, j) -> CharArrays.swap(array, i, j), start, end);
    }

    public static ForkJoinTask<Void> parallelReverse(final char @NotNull [] array) {
        return parallelReverse(array, 0, array.length);
    }

    /**
     * Performs reversion in parallel in the same way as {@link #reverse(Swapper, int, int)}
     * @param array the array whose elements will be reversed
     * @param start the first index to be swapped
     * @param end the last index + 1 to be swapped
     * @return the {@link ForkJoinTask} performing the reversion
     * @see #parallelReverse(Swapper, int, int)
     */
    public static ForkJoinTask<Void> parallelReverse(final short @NotNull [] array, int start, int end) {
        Preconditions.checkPositionIndexes(start, end, array.length);

        return parallelReverse((i, j) -> ShortArrays.swap(array, i, j), start, end);
    }

    public static ForkJoinTask<Void> parallelReverse(final short @NotNull [] array) {
        return parallelReverse(array, 0, array.length);
    }

    /**
     * Performs reversion in parallel in the same way as {@link #reverse(Swapper, int, int)}
     * @param array the array whose elements will be reversed
     * @param start the first index to be swapped
     * @param end the last index + 1 to be swapped
     * @return the {@link ForkJoinTask} performing the reversion
     * @see #parallelReverse(Swapper, int, int)
     */
    public static ForkJoinTask<Void> parallelReverse(final int @NotNull [] array, int start, int end) {
        Preconditions.checkPositionIndexes(start, end, array.length);

        return parallelReverse((i, j) -> IntArrays.swap(array, i, j), start, end);
    }

    public static ForkJoinTask<Void> parallelReverse(final int @NotNull [] array) {
        return parallelReverse(array, 0, array.length);
    }

    /**
     * Performs reversion in parallel in the same way as {@link #reverse(Swapper, int, int)}
     * @param array the array whose elements will be reversed
     * @param start the first index to be swapped
     * @param end the last index + 1 to be swapped
     * @return the {@link ForkJoinTask} performing the reversion
     * @see #parallelReverse(Swapper, int, int)
     */
    public static ForkJoinTask<Void> parallelReverse(final long @NotNull [] array, int start, int end) {
        Preconditions.checkPositionIndexes(start, end, array.length);

        return parallelReverse((i, j) -> LongArrays.swap(array, i, j), start, end);
    }

    public static ForkJoinTask<Void> parallelReverse(final long @NotNull [] array) {
        return parallelReverse(array, 0, array.length);
    }

    /**
     * Performs reversion in parallel in the same way as {@link #reverse(Swapper, int, int)}
     * @param array the array whose elements will be reversed
     * @param start the first index to be swapped
     * @param end the last index + 1 to be swapped
     * @return the {@link ForkJoinTask} performing the reversion
     * @see #parallelReverse(Swapper, int, int)
     */
    public static ForkJoinTask<Void> parallelReverse(final float @NotNull [] array, int start, int end) {
        Preconditions.checkPositionIndexes(start, end, array.length);

        return parallelReverse((i, j) -> FloatArrays.swap(array, i, j), start, end);
    }

    public static ForkJoinTask<Void> parallelReverse(final float @NotNull [] array) {
        return parallelReverse(array, 0, array.length);
    }

    /**
     * Performs reversion in parallel in the same way as {@link #reverse(Swapper, int, int)}
     * @param array the array whose elements will be reversed
     * @param start the first index to be swapped
     * @param end the last index + 1 to be swapped
     * @return the {@link ForkJoinTask} performing the reversion
     * @see #parallelReverse(Swapper, int, int)
     */
    public static ForkJoinTask<Void> parallelReverse(final double @NotNull [] array, int start, int end) {
        Preconditions.checkPositionIndexes(start, end, array.length);

        return parallelReverse((i, j) -> DoubleArrays.swap(array, i, j), start, end);
    }

    public static ForkJoinTask<Void> parallelReverse(final double @NotNull [] array) {
        return parallelReverse(array, 0, array.length);
    }

    public static boolean deepEqualsIgnoreComponentType(final Object o0, final Object o1) {
        if (o0 == o1) {
            return true;
        }

        if (o0 == null) {
            return false;
        }

        if (o0 instanceof Object[] a) {
            if (o1 instanceof Object[] b) {
                if (a.length != b.length) {
                    return false;
                }

                for (int i = 0; i < a.length; i++) {
                    if (!deepEqualsIgnoreComponentType(a[i], b[i])) {
                        return false;
                    }
                }

                return true;
            }

            if (o1 instanceof boolean[] b) {
                return equals(a, b);
            }

            if (o1 instanceof byte[] b) {
                return equals(a, b);
            }

            if (o1 instanceof char[] b) {
                return equals(a, b);
            }

            if (o1 instanceof short[] b) {
                return equals(a, b);
            }

            if (o1 instanceof int[] b) {
                return equals(a, b);
            }

            if (o1 instanceof long[] b) {
                return equals(a, b);
            }

            if (o1 instanceof float[] b) {
                return equals(a, b);
            }

            if (o1 instanceof double[] b) {
                return equals(a, b);
            }

            return false;
        }

        if (o0 instanceof boolean[] a) {
            if (o1 instanceof Object[] b) {
                return equals(b, a);
            }

            if (o1 instanceof boolean[] b) {
                return Arrays.equals(a, b);
            }

            if (o1 instanceof byte[] b) {
                return equals(a, b);
            }

            if (o1 instanceof char[] b) {
                return equals(a, b);
            }

            if (o1 instanceof short[] b) {
                return equals(a, b);
            }

            if (o1 instanceof int[] b) {
                return equals(a, b);
            }

            if (o1 instanceof long[] b) {
                return equals(a, b);
            }

            if (o1 instanceof float[] b) {
                return equals(a, b);
            }

            if (o1 instanceof double[] b) {
                return equals(a, b);
            }

            return false;
        }

        if (o0 instanceof byte[] a) {
            if (o1 instanceof Object[] b) {
                return equals(b, a);
            }

            if (o1 instanceof boolean[] b) {
                return equals(b, a);
            }

            if (o1 instanceof byte[] b) {
                return Arrays.equals(a, b);
            }

            if (o1 instanceof char[] b) {
                return equals(a, b);
            }

            if (o1 instanceof short[] b) {
                return equals(a, b);
            }

            if (o1 instanceof int[] b) {
                return equals(a, b);
            }

            if (o1 instanceof long[] b) {
                return equals(a, b);
            }

            if (o1 instanceof float[] b) {
                return equals(a, b);
            }

            if (o1 instanceof double[] b) {
                return equals(a, b);
            }

            return false;
        }

        if (o0 instanceof char[] a) {
            if (o1 instanceof Object[] b) {
                return equals(b, a);
            }

            if (o1 instanceof boolean[] b) {
                return equals(b, a);
            }

            if (o1 instanceof byte[] b) {
                return equals(b, a);
            }

            if (o1 instanceof char[] b) {
                return Arrays.equals(a, b);
            }

            if (o1 instanceof short[] b) {
                return equals(a, b);
            }

            if (o1 instanceof int[] b) {
                return equals(a, b);
            }

            if (o1 instanceof long[] b) {
                return equals(a, b);
            }

            if (o1 instanceof float[] b) {
                return equals(a, b);
            }

            if (o1 instanceof double[] b) {
                return equals(a, b);
            }

            return false;
        }

        if (o0 instanceof short[] a) {
            if (o1 instanceof Object[] b) {
                return equals(b, a);
            }

            if (o1 instanceof boolean[] b) {
                return equals(b, a);
            }

            if (o1 instanceof byte[] b) {
                return equals(b, a);
            }

            if (o1 instanceof char[] b) {
                return equals(b, a);
            }

            if (o1 instanceof short[] b) {
                return Arrays.equals(a, b);
            }

            if (o1 instanceof int[] b) {
                return equals(a, b);
            }

            if (o1 instanceof long[] b) {
                return equals(a, b);
            }

            if (o1 instanceof float[] b) {
                return equals(a, b);
            }

            if (o1 instanceof double[] b) {
                return equals(a, b);
            }

            return false;
        }

        if (o0 instanceof int[] a) {
            if (o1 instanceof Object[] b) {
                return equals(b, a);
            }

            if (o1 instanceof boolean[] b) {
                return equals(b, a);
            }

            if (o1 instanceof byte[] b) {
                return equals(b, a);
            }

            if (o1 instanceof char[] b) {
                return equals(b, a);
            }

            if (o1 instanceof short[] b) {
                return equals(b, a);
            }

            if (o1 instanceof int[] b) {
                return Arrays.equals(a, b);
            }

            if (o1 instanceof long[] b) {
                return equals(a, b);
            }

            if (o1 instanceof float[] b) {
                return equals(a, b);
            }

            if (o1 instanceof double[] b) {
                return equals(a, b);
            }

            return false;
        }

        if (o0 instanceof long[] a) {
            if (o1 instanceof Object[] b) {
                return equals(b, a);
            }

            if (o1 instanceof boolean[] b) {
                return equals(b, a);
            }

            if (o1 instanceof byte[] b) {
                return equals(b, a);
            }

            if (o1 instanceof char[] b) {
                return equals(b, a);
            }

            if (o1 instanceof short[] b) {
                return equals(b, a);
            }

            if (o1 instanceof int[] b) {
                return equals(b, a);
            }

            if (o1 instanceof long[] b) {
                return Arrays.equals(a, b);
            }

            if (o1 instanceof float[] b) {
                return equals(a, b);
            }

            if (o1 instanceof double[] b) {
                return equals(a, b);
            }

            return false;
        }

        if (o0 instanceof float[] a) {
            if (o1 instanceof Object[] b) {
                return equals(b, a);
            }

            if (o1 instanceof boolean[] b) {
                return equals(b, a);
            }

            if (o1 instanceof byte[] b) {
                return equals(b, a);
            }

            if (o1 instanceof char[] b) {
                return equals(b, a);
            }

            if (o1 instanceof short[] b) {
                return equals(b, a);
            }

            if (o1 instanceof int[] b) {
                return equals(b, a);
            }

            if (o1 instanceof long[] b) {
                return equals(b, a);
            }

            if (o1 instanceof float[] b) {
                return Arrays.equals(a, b);
            }

            if (o1 instanceof double[] b) {
                return equals(a, b);
            }

            return false;
        }

        if (o0 instanceof double[] a) {
            if (o1 instanceof Object[] b) {
                return equals(b, a);
            }

            if (o1 instanceof boolean[] b) {
                return equals(b, a);
            }

            if (o1 instanceof byte[] b) {
                return equals(b, a);
            }

            if (o1 instanceof char[] b) {
                return equals(b, a);
            }

            if (o1 instanceof short[] b) {
                return equals(b, a);
            }

            if (o1 instanceof int[] b) {
                return equals(b, a);
            }

            if (o1 instanceof long[] b) {
                return equals(b, a);
            }

            if (o1 instanceof float[] b) {
                return equals(b, a);
            }

            if (o1 instanceof double[] b) {
                return Arrays.equals(a, b);
            }

            return false;
        }

        return o0.equals(o1);
    }

    public static boolean equals(final Object[] a, final boolean... b) {
        return a == null ? b == null : b != null && ListUtil.transformToBoolean(Arrays.asList(a),
                (e) -> (boolean) e).equals(asList(b));
    }

    public static boolean equals(final Object[] a, final byte... b) {
        return a == null ? b == null : b != null && ListUtil.transformToByte(Arrays.asList(a),
                (e) -> ((Number) e).byteValue()).equals(asList(b));
    }

    public static boolean equals(final Object[] a, final char... b) {
        return a == null ? b == null : b != null && ListUtil.transformToChar(Arrays.asList(a),
                (e) -> (char) e).equals(asList(b));
    }

    public static boolean equals(final Object[] a, final short... b) {
        return a == null ? b == null : b != null && ListUtil.transformToShort(Arrays.asList(a),
                (e) -> ((Number) e).shortValue()).equals(asList(b));
    }

    public static boolean equals(final Object[] a, final int... b) {
        return a == null ? b == null : b != null && ListUtil.transformToInt(Arrays.asList(a),
                (e) -> ((Number) e).intValue()).equals(asList(b));
    }

    public static boolean equals(final Object[] a, final long... b) {
        return a == null ? b == null : b != null && ListUtil.transformToLong(Arrays.asList(a),
                (e) -> ((Number) e).longValue()).equals(asList(b));
    }

    public static boolean equals(final Object[] a, final float... b) {
        return a == null ? b == null : b != null && ListUtil.transformToFloat(Arrays.asList(a),
                (e) -> ((Number) e).floatValue()).equals(asList(b));
    }

    public static boolean equals(final Object[] a, final double... b) {
        return a == null ? b == null : b != null && ListUtil.transformToDouble(Arrays.asList(a),
                (e) -> ((Number) e).doubleValue()).equals(asList(b));
    }

    public static boolean equals(final boolean[] a, final byte... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != (b[i] != 0)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final boolean[] a, final char... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != (b[i] != 0)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final boolean[] a, final short... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != (b[i] != 0)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final boolean[] a, final int... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != (b[i] != 0)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final boolean[] a, final long... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != (b[i] != 0)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final boolean[] a, final float... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != (b[i] != 0)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final boolean[] a, final double... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != (b[i] != 0)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final byte[] a, final char... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final byte[] a, final short... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final byte[] a, final int... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final byte[] a, final long... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final byte[] a, final float... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (Float.floatToIntBits(a[i]) != Float.floatToIntBits(b[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final byte[] a, final double... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (Double.doubleToLongBits(a[i]) != Double.doubleToLongBits(b[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final char[] a, final short... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final char[] a, final int... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final char[] a, final long... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final char[] a, final float... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (Float.floatToIntBits(a[i]) != Float.floatToIntBits(b[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final char[] a, final double... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (Double.doubleToLongBits(a[i]) != Double.doubleToLongBits(b[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final short[] a, final int... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final short[] a, final long... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final short[] a, final float... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (Float.floatToIntBits(a[i]) != Float.floatToIntBits(b[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final short[] a, final double... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (Double.doubleToLongBits(a[i]) != Double.doubleToLongBits(b[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final int[] a, final long... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final int[] a, final float... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (Double.doubleToLongBits(a[i]) != Double.doubleToLongBits(b[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final int[] a, final double... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (Double.doubleToLongBits(a[i]) != Double.doubleToLongBits(b[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final long[] a, final float... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (Double.doubleToLongBits(a[i]) != Double.doubleToLongBits(b[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final long[] a, final double... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (Double.doubleToLongBits(a[i]) != Double.doubleToLongBits(b[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final float[] a, final double... b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (Double.doubleToLongBits(a[i]) != Double.doubleToLongBits(b[i])) {
                return false;
            }
        }

        return true;
    }

    public static int frequency(final boolean @NotNull [] a) {
        int frequency = 0;

        for (boolean b : a) {
            if (b) {
                frequency++;
            }
        }

        return frequency;
    }

    public static int frequency(final byte @NotNull [] a, final byte value) {
        int frequency = 0;

        for (byte b : a) {
            if (b == value) {
                frequency++;
            }
        }

        return frequency;
    }

    public static int frequency(final short @NotNull [] a, final short value) {
        int frequency = 0;

        for (short b : a) {
            if (b == value) {
                frequency++;
            }
        }

        return frequency;
    }

    public static int frequency(final int @NotNull [] a, final int value) {
        int frequency = 0;

        for (int b : a) {
            if (b == value) {
                frequency++;
            }
        }

        return frequency;
    }

    public static int frequency(final long @NotNull [] a, final long value) {
        int frequency = 0;

        for (long b : a) {
            if (b == value) {
                frequency++;
            }
        }

        return frequency;
    }

    public static int frequency(final char @NotNull [] a, final char value) {
        int frequency = 0;

        for (char b : a) {
            if (b == value) {
                frequency++;
            }
        }

        return frequency;
    }

    public static int frequency(final float @NotNull [] a, final float value) {
        int frequency = 0;

        for (float b : a) {
            if (b == value) {
                frequency++;
            }
        }

        return frequency;
    }

    public static int frequency(final double @NotNull [] a, final double value) {
        int frequency = 0;

        for (double b : a) {
            if (b == value) {
                frequency++;
            }
        }

        return frequency;
    }

    @SafeVarargs
    public static <E> List<E> asList(final E @NotNull ... array) {
        return Arrays.asList(array);
    }

    public static BooleanArrayAsList asList(boolean @NotNull ... array) {
        return new BooleanArrayAsList(array);
    }

    public static ByteArrayAsList asList(byte @NotNull ... array) {
        return new ByteArrayAsList(array);
    }

    public static ShortArrayAsList asList(short @NotNull ... array) {
        return new ShortArrayAsList(array);
    }

    public static IntArrayAsList asList(int @NotNull ... array) {
        return new IntArrayAsList(array);
    }

    public static LongArrayAsList asList(long @NotNull ... array) {
        return new LongArrayAsList(array);
    }

    public static CharArrayAsList asList(char @NotNull ... array) {
        return new CharArrayAsList(array);
    }

    public static FloatArrayAsList asList(float @NotNull ... array) {
        return new FloatArrayAsList(array);
    }

    public static DoubleArrayAsList asList(double @NotNull ... array) {
        return new DoubleArrayAsList(array);
    }

    public static BooleanStream stream(final boolean @NotNull ... array) {
        return stream(array, 0, array.length);
    }

    public static BooleanStream stream(final boolean @NotNull [] array, final int startingIndex, final int endingIndex) {
        return StreamUtil.booleanStream(spliterator(array, startingIndex, endingIndex), false);
    }

    public static ByteStream stream(final byte @NotNull ... array) {
        return stream(array, 0, array.length);
    }

    public static ByteStream stream(final byte @NotNull [] array, final int startingIndex, final int endingIndex) {
        return StreamUtil.byteStream(spliterator(array, startingIndex, endingIndex), false);
    }

    public static ShortStream stream(final short @NotNull ... array) {
        return stream(array, 0, array.length);
    }

    public static ShortStream stream(final short @NotNull [] array, final int startingIndex, final int endingIndex) {
        return StreamUtil.shortStream(spliterator(array, startingIndex, endingIndex), false);
    }

    public static CharStream stream(final char @NotNull ... array) {
        return stream(array, 0, array.length);
    }

    public static CharStream stream(final char @NotNull [] array, final int startingIndex, final int endingIndex) {
        return StreamUtil.charStream(spliterator(array, startingIndex, endingIndex), false);
    }

    public static FloatStream stream(final float @NotNull ... array) {
        return stream(array, 0, array.length);
    }

    public static FloatStream stream(final float @NotNull [] array, final int startingIndex, final int endingIndex) {
        return StreamUtil.floatStream(spliterator(array, startingIndex, endingIndex), false);
    }

    public static BooleanSpliterator spliterator(final boolean @NotNull [] array, final int startInclusive,
                                                 final int endExclusive) {
        return BooleanSpliterators.wrap(array, startInclusive, endExclusive);
    }

    public static ByteSpliterator spliterator(final byte @NotNull [] array, final int startInclusive,
                                              final int endExclusive) {
        return ByteSpliterators.wrap(array, startInclusive, endExclusive);
    }

    public static ShortSpliterator spliterator(final short @NotNull [] array, final int startInclusive,
                                               final int endExclusive) {
        return ShortSpliterators.wrap(array, startInclusive, endExclusive);
    }

    public static CharSpliterator spliterator(final char @NotNull [] array, final int startInclusive,
                                              final int endExclusive) {
        return CharSpliterators.wrap(array, startInclusive, endExclusive);
    }

    public static FloatSpliterator spliterator(final float @NotNull [] array, final int startInclusive,
                                               final int endExclusive) {
        return FloatSpliterators.wrap(array, startInclusive, endExclusive);
    }

    public static final class BooleanArrayAsList extends AbstractBooleanList
            implements ImprovedBooleanCollection, RandomAccess, Cloneable {
        private final boolean[] array;

        public BooleanArrayAsList(boolean[] array) {
            this.array = Preconditions.checkNotNull(array);
        }

        @Override
        public BooleanArrayAsList clone() {
            return asList(array.clone());
        }

        @Override
        public BooleanSpliterator spliterator() {
            return ArrayUtil.spliterator(array, 0, array.length);
        }

        @Override
        public void getElements(int from, boolean @NotNull [] a, int offset, int length) {
            System.arraycopy(array, from, a, offset, length);
        }

        @Override
        public void setElements(int index, boolean @NotNull [] a, int offset, int length) {
            System.arraycopy(a, offset, array, index, length);
        }

        @Override
        public void replaceAll(final @NotNull BooleanUnaryOperator operator) {
            setAll(array, (i) -> operator.apply(array[i]));
        }

        @Override
        public boolean getBoolean(int index) {
            return array[index];
        }

        @Override
        public void sort(BooleanComparator comparator) {
            BooleanArrays.quickSort(array, comparator);
        }

        @Override
        public void unstableSort(BooleanComparator comparator) {
            BooleanArrays.unstableSort(array, comparator);
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public boolean set(int index, boolean k) {
            boolean result = getBoolean(index);
            array[index] = k;
            return result;
        }

        public boolean[] getArray() {
            return array;
        }
    }

    public static final class ByteArrayAsList extends AbstractByteList
            implements ImprovedByteCollection, RandomAccess, Cloneable {
        private final byte[] array;

        public ByteArrayAsList(byte[] array) {
            this.array = Preconditions.checkNotNull(array);
        }

        @Override
        public ByteArrayAsList clone() {
            return asList(array.clone());
        }

        @Override
        public ByteSpliterator spliterator() {
            return ArrayUtil.spliterator(array, 0, array.length);
        }

        @Override
        public void getElements(int from, byte @NotNull [] a, int offset, int length) {
            System.arraycopy(array, from, a, offset, length);
        }

        @Override
        public void setElements(int index, byte @NotNull [] a, int offset, int length) {
            System.arraycopy(a, offset, array, index, length);
        }

        @Override
        public void replaceAll(final @NotNull ByteUnaryOperator operator) {
            setAll(array, (i) -> operator.apply(array[i]));
        }

        @Override
        public byte getByte(int index) {
            return array[index];
        }

        @Override
        public void sort(ByteComparator comparator) {
            ByteArrays.quickSort(array, comparator);
        }

        @Override
        public void unstableSort(ByteComparator comparator) {
            ByteArrays.unstableSort(array, comparator);
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public byte set(int index, byte k) {
            byte result = getByte(index);
            array[index] = k;
            return result;
        }

        public byte[] getArray() {
            return array;
        }
    }

    public static final class ShortArrayAsList extends AbstractShortList
            implements ImprovedShortCollection, RandomAccess, Cloneable {
        private final short[] array;

        public ShortArrayAsList(short[] array) {
            this.array = Preconditions.checkNotNull(array);
        }

        @Override
        public ShortArrayAsList clone() {
            return asList(array.clone());
        }

        @Override
        public ShortSpliterator spliterator() {
            return ArrayUtil.spliterator(array, 0, array.length);
        }

        @Override
        public void getElements(int from, short @NotNull [] a, int offset, int length) {
            System.arraycopy(array, from, a, offset, length);
        }

        @Override
        public void setElements(int index, short @NotNull [] a, int offset, int length) {
            System.arraycopy(a, offset, array, index, length);
        }

        @Override
        public void replaceAll(final @NotNull ShortUnaryOperator operator) {
            setAll(array, (i) -> operator.apply(array[i]));
        }

        @Override
        public short getShort(int index) {
            return array[index];
        }

        @Override
        public void sort(ShortComparator comparator) {
            ShortArrays.quickSort(array, comparator);
        }

        @Override
        public void unstableSort(ShortComparator comparator) {
            ShortArrays.unstableSort(array, comparator);
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public short set(int index, short k) {
            short result = getShort(index);
            array[index] = k;
            return result;
        }

        public short[] getArray() {
            return array;
        }
    }

    public static final class IntArrayAsList extends AbstractIntList implements RandomAccess, Cloneable {
        private final int[] array;

        public IntArrayAsList(int[] array) {
            this.array = Preconditions.checkNotNull(array);
        }

        @Override
        public IntArrayAsList clone() {
            return asList(array.clone());
        }

        @Override
        public IntSpliterator spliterator() {
            return IntSpliterators.wrap(array, 0, array.length);
        }

        @Override
        public void getElements(int from, int @NotNull [] a, int offset, int length) {
            System.arraycopy(array, from, a, offset, length);
        }

        @Override
        public void setElements(int index, int @NotNull [] a, int offset, int length) {
            System.arraycopy(a, offset, array, index, length);
        }

        @Override
        public void replaceAll(final @NotNull IntUnaryOperator operator) {
            Arrays.setAll(array, (i) -> operator.applyAsInt(array[i]));
        }

        @Override
        public int getInt(int index) {
            return array[index];
        }

        @Override
        public void sort(IntComparator comparator) {
            IntArrays.quickSort(array, comparator);
        }

        @Override
        public void unstableSort(IntComparator comparator) {
            IntArrays.unstableSort(array, comparator);
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public int set(int index, int k) {
            int result = getInt(index);
            array[index] = k;
            return result;
        }

        public int[] getArray() {
            return array;
        }
    }

    public static final class LongArrayAsList extends AbstractLongList implements RandomAccess, Cloneable {
        private final long[] array;

        public LongArrayAsList(long[] array) {
            this.array = Preconditions.checkNotNull(array);
        }

        @Override
        public LongArrayAsList clone() {
            return asList(array.clone());
        }

        @Override
        public LongSpliterator spliterator() {
            return LongSpliterators.wrap(array, 0, array.length);
        }

        @Override
        public void getElements(int from, long @NotNull [] a, int offset, int length) {
            System.arraycopy(array, from, a, offset, length);
        }

        @Override
        public void setElements(int index, long @NotNull [] a, int offset, int length) {
            System.arraycopy(a, offset, array, index, length);
        }

        @Override
        public void replaceAll(final @NotNull LongUnaryOperator operator) {
            Arrays.setAll(array, (i) -> operator.applyAsLong(array[i]));
        }

        @Override
        public long getLong(int index) {
            return array[index];
        }

        @Override
        public void sort(LongComparator comparator) {
            LongArrays.quickSort(array, comparator);
        }

        @Override
        public void unstableSort(LongComparator comparator) {
            LongArrays.unstableSort(array, comparator);
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public long set(int index, long k) {
            long result = getLong(index);
            array[index] = k;
            return result;
        }

        public long[] getArray() {
            return array;
        }
    }

    public static final class CharArrayAsList extends AbstractCharList
            implements ImprovedCharCollection, RandomAccess, Cloneable {
        private final char[] array;

        public CharArrayAsList(char[] array) {
            this.array = Preconditions.checkNotNull(array);
        }

        @Override
        public CharArrayAsList clone() {
            return asList(array.clone());
        }

        @Override
        public CharSpliterator spliterator() {
            return ArrayUtil.spliterator(array, 0, array.length);
        }

        @Override
        public void getElements(int from, char @NotNull [] a, int offset, int length) {
            System.arraycopy(array, from, a, offset, length);
        }

        @Override
        public void setElements(int index, char @NotNull [] a, int offset, int length) {
            System.arraycopy(a, offset, array, index, length);
        }

        @Override
        public void replaceAll(final @NotNull CharUnaryOperator operator) {
            setAll(array, (i) -> operator.apply(array[i]));
        }

        @Override
        public char getChar(int index) {
            return array[index];
        }

        @Override
        public void sort(CharComparator comparator) {
            CharArrays.quickSort(array, comparator);
        }

        @Override
        public void unstableSort(CharComparator comparator) {
            CharArrays.unstableSort(array, comparator);
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public char set(int index, char k) {
            char result = getChar(index);
            array[index] = k;
            return result;
        }

        public char[] getArray() {
            return array;
        }
    }

    public static final class FloatArrayAsList extends AbstractFloatList
            implements ImprovedFloatCollection, RandomAccess, Cloneable {
        private final float[] array;

        public FloatArrayAsList(float[] array) {
            this.array = Preconditions.checkNotNull(array);
        }

        @Override
        public FloatSpliterator spliterator() {
            return ArrayUtil.spliterator(array, 0, array.length);
        }

        @Override
        public FloatArrayAsList clone() {
            return asList(array.clone());
        }

        @Override
        public float[] toFloatArray() {
            return array.clone();
        }

        @Override
        public void getElements(int from, float @NotNull [] a, int offset, int length) {
            System.arraycopy(array, from, a, offset, length);
        }

        @Override
        public void setElements(int index, float @NotNull [] a, int offset, int length) {
            System.arraycopy(a, offset, array, index, length);
        }

        @Override
        public void replaceAll(final @NotNull FloatUnaryOperator operator) {
            setAll(array, (i) -> operator.apply(array[i]));
        }

        @Override
        public float getFloat(int index) {
            return array[index];
        }

        @Override
        public void sort(FloatComparator comparator) {
            FloatArrays.quickSort(array, comparator);
        }

        @Override
        public void unstableSort(FloatComparator comparator) {
            FloatArrays.unstableSort(array, comparator);
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public float set(int index, float k) {
            float result = getFloat(index);
            array[index] = k;
            return result;
        }

        public float[] getArray() {
            return array;
        }
    }

    public static final class DoubleArrayAsList extends AbstractDoubleList implements RandomAccess, Cloneable {
        private final double[] array;

        public DoubleArrayAsList(double[] array) {
            this.array = Preconditions.checkNotNull(array);
        }

        @Override
        public DoubleArrayAsList clone() {
            return asList(array.clone());
        }

        @Override
        public DoubleSpliterator spliterator() {
            return DoubleSpliterators.wrap(array, 0, array.length);
        }

        @Override
        public void getElements(int from, double @NotNull [] a, int offset, int length) {
            System.arraycopy(array, from, a, offset, length);
        }

        @Override
        public void setElements(int index, double @NotNull [] a, int offset, int length) {
            System.arraycopy(a, offset, array, index, length);
        }

        @Override
        public void replaceAll(final @NotNull DoubleUnaryOperator operator) {
            Arrays.setAll(array, (i) -> operator.applyAsDouble(array[i]));
        }

        @Override
        public double getDouble(int index) {
            return array[index];
        }

        @Override
        public void sort(DoubleComparator comparator) {
            DoubleArrays.quickSort(array, comparator);
        }

        @Override
        public void unstableSort(DoubleComparator comparator) {
            DoubleArrays.unstableSort(array, comparator);
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public double set(int index, double k) {
            double result = getDouble(index);
            array[index] = k;
            return result;
        }

        public double[] getArray() {
            return array;
        }
    }

    private static final class ParallelArrayReverseTask extends RecursiveAction {
        private final int from;
        private final int to;
        private final Swapper swapper;

        private ParallelArrayReverseTask(int from, int to, Swapper swapper) {
            this.from = from;
            this.to = to;
            this.swapper = swapper;
        }

        @Override
        protected void compute() {
            reverse(swapper, from, to);
        }
    }

    private static final class ForkJoinArrayReverseTask extends RecursiveAction {
        private final int from;
        private final int to;
        private final int fence;
        private final Swapper swapper;

        private ForkJoinArrayReverseTask(int from, int to, int fence, Swapper swapper) {
            this.from = from;
            this.to = to;
            this.fence = fence;
            this.swapper = swapper;
        }

        @Override
        protected void compute() {
            ForkJoinPool pool = getPool();
            int length = to - from;

            if (pool.getParallelism() == 1 || length < PARALLEL_REVERSE_NO_FORK) {
                reverse(swapper, from, to, fence);
            } else {
                int i = from;
                int lastIndex = from + Math.floorDiv(to - from, PARALLEL_REVERSE_NO_FORK);
                final ParallelArrayReverseTask[] tasks =
                        new ParallelArrayReverseTask[(lastIndex - i) / PARALLEL_REVERSE_NO_FORK + 1];
                int index = 0;

                while (i < lastIndex) {
                    tasks[index++] = (ParallelArrayReverseTask) pool.submit(new ParallelArrayReverseTask(i,
                            i += PARALLEL_REVERSE_NO_FORK, swapper));
                }

                tasks[index] = (ParallelArrayReverseTask) pool.submit(new ParallelArrayReverseTask(i, to, swapper));

                for (ParallelArrayReverseTask task : tasks) {
                    task.join();
                }
            }
        }
    }

    public static final class ForkJoinArrayUpdateTask extends RecursiveAction {
        private final int from;
        private final int to;
        private final IntConsumer updater;

        private ForkJoinArrayUpdateTask(IntConsumer updater, int from, int to) {
            this.from = from;
            this.to = to;
            this.updater = updater;
        }

        @Override
        protected void compute() {
            final ForkJoinPool pool = getPool();
            final int parallelism = Math.min(pool.getParallelism(), to - from);

            if (parallelism <= 1) {
                for (int i = from; i < to; i++) {
                    updater.accept(i);
                }

                return;
            }

            final ParallelArrayUpdateTask[] tasks = new ParallelArrayUpdateTask[parallelism];
            final AtomicInteger index = new AtomicInteger(from);

            for (int i = 0; i < parallelism; i++) {
                tasks[i] = (ParallelArrayUpdateTask) pool.submit(new ParallelArrayUpdateTask(updater, index, to));
            }

            for (ParallelArrayUpdateTask task : tasks) {
                task.join();
            }
        }
    }

    private static final class ParallelArrayUpdateTask extends RecursiveAction {
        private final IntConsumer updater;
        private final AtomicInteger index;
        private final int fence;

        private ParallelArrayUpdateTask(IntConsumer updater, AtomicInteger index, int fence) {
            this.index = index;
            this.updater = updater;
            this.fence = fence;
        }

        @Override
        protected void compute() {
            int i;

            while ((i = index.getAndIncrement()) < fence) {
                updater.accept(i);
            }

            index.set(fence); // avoid integer overflow in rare cases
        }
    }
}
