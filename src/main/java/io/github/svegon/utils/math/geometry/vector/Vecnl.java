package io.github.svegon.utils.math.geometry.vector;

import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.fast.util.ints.objects.Int2ObjectTableMap;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.jetbrains.annotations.Nullable;

public final class Vecnl extends Vecnt<long[], Vecnl> {
    private static final Int2ObjectMap<Vecnl> ZERO_VECTORS = new Int2ObjectTableMap<>();

    private final long[] values;

    Vecnl(long... values) {
        this.values = values;
    }

    /**
     * creates a new Vecnl with the given number of dimensions
     * its value may be in undefined state and should be overwritten
     *
     * @param size
     */
    Vecnl(int size) {
        this(new long[size]);
    }

    Vecnl(Vecnl model) {
        this(model.values.clone());
    }

    @Override
    public long[] toPrimitiveArray() {
        return values.clone();
    }

    @Override
    public long[] toPrimitiveArray(long @Nullable [] longs) {
        if (longs == null || longs.length < size()) {
            return toPrimitiveArray();
        }

        System.arraycopy(values, 0, longs, 0, size());

        return longs;
    }

    @Override
    public Vecnl add(Vecnl other) {
        Preconditions.checkArgument(size() == other.size());

        return add(other.values);
    }

    @Override
    public Vecnl substract(Vecnl other) {
        Preconditions.checkArgument(size() == other.size());

        return substract(other.values);
    }

    @Override
    public Vecnl multiply(final double multiplier) {
        if (multiplier == 0) {
            return zero(size());
        }

        if (multiplier == 1) {
            return this;
        }

        final Vecnl result = new Vecnl(size());

        ArrayUtil.parallelSetAll(result.values, i -> (long) (values[i] * multiplier)).join();

        return result;
    }

    @Override
    public Vecnl multiplyEach(Vecnl other) {
        Preconditions.checkArgument(size() == other.size());

        final Vecnl result = new Vecnl(size());

        ArrayUtil.parallelSetAll(result.values, i -> values[i] * other.values[i]).join();

        return result;
    }

    @Override
    public double getDouble(int index) {
        return getLong(index);
    }

    @Override
    public int size() {
        return values.length;
    }

    public long getLong(int index) {
        return values[index];
    }

    public Vecnl add(long... addends) {
        final Vecnl result = new Vecnl(size());

        ArrayUtil.parallelSetAll(result.values, i -> values[i] + addends[i]).join();

        return result;
    }

    public Vecnl substract(long... substractions) {
        final Vecnl result = new Vecnl(size());

        ArrayUtil.parallelSetAll(result.values, i -> values[i] - substractions[i]).join();

        return result;
    }

    public Vecnl multiplyEach(double... multipliers) {
        final Vecnl result = new Vecnl(size());

        ArrayUtil.parallelSetAll(result.values, i -> (long) (values[i] * multipliers[i])).join();

        return result;
    }

    public Vecnl crossProduct(Vecnl other) {
        Preconditions.checkArgument(size() == other.size());

        return switch (size()) {
            case 0, 1 -> zero(3);
            case 2 -> new Vecnl(0, 0, values[0] * other.values[1] - values[1] * other.values[0]);
            case 3 -> new Vecnl(values[1] * other.values[2] - values[2] * other.values[1],
                    values[2] * other.values[0] - values[0] * other.values[2],
                    values[0] * other.values[1] - values[1] * other.values[0]);
            default -> throw new IllegalArgumentException();
        };
    }

    public static Vecnl of(long... values) {
        return new Vecnl(values.clone());
    }

    public static Vecnl of(int... values) {
        Vecnl vector = new Vecnl(values.length);

        ArrayUtil.parallelSetAll(vector.values, i -> values[i]).join();

        return vector;
    }

    public static Vecnl of(float... values) {
        Vecnl vector = new Vecnl(values.length);

        ArrayUtil.parallelSetAll(vector.values, i -> (long) values[i]).join();

        return vector;
    }

    public static Vecnl of(double... values) {
        Vecnl vector = new Vecnl(values.length);

        ArrayUtil.parallelSetAll(vector.values, i -> (long) values[i]).join();

        return vector;
    }

    public static Vecnl of(Vecnt<?, ?> vector) {
        if (vector instanceof Vecnl) {
            return (Vecnl) vector;
        }

        Vecnl result = new Vecnl(vector.size());

        ArrayUtil.parallelSetAll(result.values, vector instanceof Vecni vecni ? (Int2LongFunction) vecni::getInt
                : (Int2LongFunction) i -> (long) vector.getDouble(i)).join();

        return result;
    }

    public static Vecnl zero(int dimensions) {
        return ZERO_VECTORS.computeIfAbsent(dimensions, k -> new Vecnl(new long[k]));
    }
}
