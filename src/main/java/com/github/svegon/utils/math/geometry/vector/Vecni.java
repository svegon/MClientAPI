package com.github.svegon.utils.math.geometry.vector;

import com.github.svegon.utils.collections.ArrayUtil;
import com.github.svegon.utils.fast.util.ints.objects.Int2ObjectTableMap;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.jetbrains.annotations.Nullable;

public final class Vecni extends Vecnt<int[], Vecni> {
    private static final Int2ObjectMap<Vecni> ZERO_VECTORS = new Int2ObjectTableMap<>();

    private final int[] values;

    Vecni(int... values) {
        this.values = values;
    }

    /**
     * creates a new Vecni with the given number of dimensions
     * its value may be in undefined state and should be overwritten
     *
     * @param size
     */
    Vecni(int size) {
        this(new int[size]);
    }

    Vecni(Vecni model) {
        this(model.values.clone());
    }

    @Override
    public int[] toPrimitiveArray() {
        return values.clone();
    }

    @Override
    public int[] toPrimitiveArray(int @Nullable [] ints) {
        if (ints == null || ints.length < size()) {
            return toPrimitiveArray();
        }

        System.arraycopy(values, 0, ints, 0, size());

        return ints;
    }

    @Override
    public Vecni add(Vecni other) {
        Preconditions.checkArgument(size() == other.size());

        return add(other.values);
    }

    @Override
    public Vecni substract(Vecni other) {
        Preconditions.checkArgument(size() == other.size());

        return substract(other.values);
    }

    @Override
    public Vecni multiply(final double multiplier) {
        if (multiplier == 0) {
            return zero(size());
        }

        if (multiplier == 1) {
            return this;
        }

        final Vecni result = new Vecni(size());

        ArrayUtil.parallelSetAll(result.values, i -> (int) (values[i] * multiplier)).join();

        return result;
    }

    @Override
    public Vecni multiplyEach(Vecni other) {
        Preconditions.checkArgument(size() == other.size());

        final Vecni result = new Vecni(size());

        ArrayUtil.parallelSetAll(result.values, i -> values[i] * other.values[i]).join();

        return result;
    }

    @Override
    public double getDouble(int index) {
        return getInt(index);
    }

    @Override
    public int size() {
        return values.length;
    }

    public int getInt(int index) {
        return values[index];
    }

    public Vecni add(int... addends) {
        final Vecni result = new Vecni(size());

        ArrayUtil.parallelSetAll(result.values, i -> values[i] + addends[i]).join();

        return result;
    }

    public Vecni substract(int... substractions) {
        final Vecni result = new Vecni(size());

        ArrayUtil.parallelSetAll(result.values, i -> values[i] - substractions[i]).join();

        return result;
    }

    public Vecni multiplyEach(double... multipliers) {
        final Vecni result = new Vecni(size());

        ArrayUtil.parallelSetAll(result.values, i -> (int) (values[i] * multipliers[i])).join();

        return result;
    }

    public Vecni crossProduct(Vecni other) {
        Preconditions.checkArgument(size() == other.size());

        return switch (size()) {
            case 0, 1 -> zero(3);
            case 2 -> new Vecni(0, 0, values[0] * other.values[1] - values[1] * other.values[0]);
            case 3 -> new Vecni(values[1] * other.values[2] - values[2] * other.values[1],
                    values[2] * other.values[0] - values[0] * other.values[2],
                    values[0] * other.values[1] - values[1] * other.values[0]);
            default -> throw new IllegalArgumentException();
        };
    }

    public static Vecni of(int... values) {
        return new Vecni(values.clone());
    }

    public static Vecni of(long... values) {
        Vecni vector = new Vecni(values.length);

        ArrayUtil.parallelSetAll(vector.values, i -> (int) values[i]).join();

        return vector;
    }

    public static Vecni of(float... values) {
        Vecni vector = new Vecni(values.length);

        ArrayUtil.parallelSetAll(vector.values, i -> (int) values[i]).join();

        return vector;
    }

    public static Vecni of(double... values) {
        Vecni vector = new Vecni(values.length);

        ArrayUtil.parallelSetAll(vector.values, i -> (int) values[i]).join();

        return vector;
    }

    public static Vecni of(Vecnt<?, ?> vector) {
        if (vector instanceof Vecni) {
            return (Vecni) vector;
        }

        Vecni result = new Vecni(vector.size());

        ArrayUtil.parallelSetAll(result.values, vector instanceof Vecnl vecnl ? i -> (int) vecnl.getLong(i)
                : i -> (int) vector.getDouble(i)).join();

        return result;
    }

    public static Vecni zero(int dimensions) {
        return ZERO_VECTORS.computeIfAbsent(dimensions, k -> new Vecni(new int[k]));
    }
}
