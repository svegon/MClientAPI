package io.github.svegon.utils.math.matrix;

import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.collections.ListUtil;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.RandomAccess;
import java.util.StringJoiner;

public final class Matrixd implements Matrix<double[], DoubleList>, Cloneable, RandomAccess {
    private final int width;
    private final int height;
    private final double[] values;

    public Matrixd(final int width, final int height, final double @NotNull ... values) {
        Preconditions.checkArgument(width > 0);
        Preconditions.checkArgument(height > 0);

        this.width = width;
        this.height = height;

        if (values.length == width * height) {
            this.values = values;
        } else {
            this.values = Arrays.copyOf(values, width * height);
        }
    }

    public Matrixd(final int width, final int height) {
        this(width, height, DoubleArrays.DEFAULT_EMPTY_ARRAY);
    }

    /**
     * instantiate as a column vector
     * @param values
     */
    public Matrixd(final double @NotNull ... values) {
        this(1, values.length, values);
    }

    public Matrixd(final @NotNull AffineTransform transform) {
        this(3, 2, transform.getScaleX(), transform.getShearY(),
                transform.getShearX(), transform.getScaleY(),
                transform.getTranslateX(), transform.getTranslateY());
    }

    public Matrixd(final @NotNull Matrix<?, ?> original) {
        this(original.width(), original.height(), copyToDoubleArray(original.asArray()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Matrix m)) {
            return false;
        }

        return width() == m.width() && height() == m.height()
                && ArrayUtil.deepEqualsIgnoreComponentType(asArray(), m.asArray());
    }

    @Override
    public int hashCode() {
        return ((31 * Integer.hashCode(width)) + Integer.hashCode(height)) * 31 + Arrays.hashCode(values);
    }

    @Override
    public Matrixd clone() {
        return new Matrixd(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(5 * width() * height()).append("[");
        StringJoiner joiner = new StringJoiner(", ", " ", "");

        for (double f : getRow(0)) {
            joiner.add(String.valueOf(f));
        }

        builder.append(joiner);

        for (int i = 1; i < width(); i++) {
            builder.append(",\n");

            joiner = new StringJoiner(", ", " ", "");

            for (double f : getRow(i)) {
                joiner.add(String.valueOf(f));
            }

            builder.append(" ").append(joiner);
        }

        return builder.append(']').toString();
    }

    @Override
    public double @NotNull [] asArray() {
        return values;
    }

    @Override
    public DoubleList getColumn(int index) {
        return ArrayUtil.asList(values).subList(height * index, height * (index + 1));
    }

    @Override
    public DoubleList getRow(int index) {
        return new Row(values, width, height, Preconditions.checkElementIndex(index, width()));
    }

    @Override
    public void set(double @NotNull ... values) {
        System.arraycopy(values, 0, this.values, 0, Math.min(this.values.length, values.length));
    }

    @Override
    public void transpose() {
        int i = 0;

        while (i < width()){
            int j = i + 1;

            while (j < height()) {
                int index = height() * j + i;
                int index2 = j++ + height() * i;

                double f = values[index2];
                values[index2] = values[index];
                values[index] = f;
            }

            i++;
        }
    }

    @Override
    public @NotNull Matrix<double[], DoubleList> transposed() {
        Matrixd copy = clone();
        copy.transpose();
        return copy;
    }

    @Override
    public void mul(@NotNull Matrix<double[], DoubleList> m) {
        Preconditions.checkArgument(width() == m.width());
        Preconditions.checkArgument(height() == m.height());

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                values[height * i + j] *= m.getColumn(i).getDouble(j);
            }
        }
    }

    @Override
    public @NotNull Matrixd multiply(@NotNull Matrix<double[], DoubleList> m) {
        Preconditions.checkArgument(width == m.height());

        Matrixd matrix = new Matrixd(m.width(), height);

        for (int i = 0; i < matrix.height; i++) {
            for (int j = 0; j < matrix.width; j++) {
                for (int k = 0; k < height; k++) {
                    matrix.values[i + j * matrix.height] += values[i + k * height];
                }
            }
        }

        return matrix;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public double[] multiply(final double @NotNull ... vector) {
        if (vector.length == 0) {
            return vector;
        }

        final double[] subject = Arrays.copyOf(vector, width());
        final int effectiveHeight = Math.min(height(), width());


        if (vector.length < width()) {
            ArrayUtil.fill(subject, vector.length, width(), 1);
        }

        for (int i = 0; i < width(); i++) {
            double value = 0;
            int j = 0;

            while (j < effectiveHeight) {
                value += subject[j] * values[j++ * height() + i];
            }

            while (j < width()) {
                value += subject[j++];
            }

            if (i < vector.length) {
                vector[i] = value;
            } else {
                final double finalValue = value;
                ArrayUtil.setRange(vector, (k) -> vector[k] / finalValue, 0, vector.length);
            }
        }

        return vector;
    }

    private static double[] copyToDoubleArray(final @NotNull Object src) {
        if (src instanceof double[] a) {
            return a.clone();
        }

        if (src instanceof boolean[] a) {
            return ListUtil.mapToDouble(ArrayUtil.asList(a), (boolean bl) -> bl ? 1 : 0).toDoubleArray();
        }

        if (src instanceof byte[] a) {
            return ListUtil.mapToDouble(ArrayUtil.asList(a), (byte e) -> e).toDoubleArray();
        }

        if (src instanceof char[] a) {
            return ListUtil.mapToDouble(ArrayUtil.asList(a), (char e) -> e).toDoubleArray();
        }

        if (src instanceof short[] a) {
            return ListUtil.mapToDouble(ArrayUtil.asList(a), (short e) -> e).toDoubleArray();
        }

        if (src instanceof int[] a) {
            return ListUtil.mapToDouble(ArrayUtil.asList(a), (int e) -> e).toDoubleArray();
        }

        if (src instanceof long[] a) {
            return ListUtil.mapToDouble(ArrayUtil.asList(a), (long e) -> e).toDoubleArray();
        }

        if (src instanceof float[] a) {
            return ListUtil.mapToDouble(ArrayUtil.asList(a), (float e) -> e).toDoubleArray();
        }

        if (src instanceof Number[] a) {
            return ListUtil.transformToDouble(ArrayUtil.asList(a), Number::doubleValue).toDoubleArray();
        }

        throw new IllegalStateException("invalid matrix'es array: " + src);
    }

    private static final class Row extends AbstractDoubleList implements RandomAccess {
        private final double[] values;
        private final int width;
        private final int height;
        private final int offset;

        private Row(double[] values, int width, int height, int offset) {
            this.values = values;
            this.width = width;
            this.height = height;
            this.offset = offset;
        }

        @Override
        public double set(int index, double k) {
            final int aIndex = height * index + offset;
            final double ret = values[aIndex];

            values[aIndex] = k;

            return ret;
        }

        @Override
        public double getDouble(int index) {
            final int aIndex = height * index + offset;

            return values[aIndex];
        }

        @Override
        public int size() {
            return width;
        }
    }
}
