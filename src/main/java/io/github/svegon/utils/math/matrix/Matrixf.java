package io.github.svegon.utils.math.matrix;

import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.collections.ListUtil;
import io.github.svegon.utils.fast.util.floats.ImprovedFloatCollection;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.floats.AbstractFloatList;
import it.unimi.dsi.fastutil.floats.FloatArrays;
import it.unimi.dsi.fastutil.floats.FloatList;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.AffineTransform;
import java.util.*;

public final class Matrixf implements Matrix<float[], FloatList>, Cloneable, RandomAccess {
    private final int width;
    private final int height;
    private final float[] values;

    public Matrixf(final int width, final int height, final float @NotNull ... values) {
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

    public Matrixf(final int width, final int height) {
        this(width, height, FloatArrays.DEFAULT_EMPTY_ARRAY);
    }

    /**
     * instantiate as a column vector
     * @param values
     */
    public Matrixf(final float @NotNull ... values) {
        this(1, values.length, values);
    }

    public Matrixf(final @NotNull AffineTransform transform) {
        this(3, 2, (float) transform.getScaleX(), (float) transform.getShearY(),
                (float) transform.getShearX(), (float) transform.getScaleY(),
                (float) transform.getTranslateX(), (float) transform.getTranslateY());
    }

    public Matrixf(final @NotNull Matrix<?, ?> original) {
        this(original.width(), original.height(), copyToFloatArray(original.asArray()));
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
    public Matrixf clone() {
        return new Matrixf(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(5 * width() * height()).append("[");
        StringJoiner joiner = new StringJoiner(", ", " ", "");

        for (float f : getRow(0)) {
            joiner.add(String.valueOf(f));
        }

        builder.append(joiner);

        for (int i = 1; i < width(); i++) {
            builder.append(",\n");

            joiner = new StringJoiner(", ", " ", "");

            for (float f : getRow(i)) {
                joiner.add(String.valueOf(f));
            }

            builder.append(" ").append(joiner);
        }

        return builder.append(']').toString();
    }

    @Override
    public float @NotNull [] asArray() {
        return values;
    }

    @Override
    public FloatList getColumn(int index) {
        return ArrayUtil.asList(values).subList(height * index, height * (index + 1));
    }

    @Override
    public FloatList getRow(int index) {
        return new Row(values, width, height, Preconditions.checkElementIndex(index, width()));
    }

    @Override
    public void set(float @NotNull ... values) {
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

                float f = values[index2];
                values[index2] = values[index];
                values[index] = f;
            }

            i++;
        }
    }

    @Override
    public @NotNull Matrixf transposed() {
        Matrixf copy = clone();
        copy.transpose();
        return copy;
    }

    @Override
    public void mul(@NotNull Matrix<float[], FloatList> m) {
        Preconditions.checkArgument(width() == m.width());
        Preconditions.checkArgument(height() == m.height());

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                values[height * i + j] *= m.getColumn(i).getFloat(j);
            }
        }
    }

    @Override
    public @NotNull Matrixf multiply(@NotNull Matrix<float[], FloatList> m) {
        Preconditions.checkArgument(width() == m.height());

        Matrixf matrix = new Matrixf(m.width(), height());

        for (int i = 0; i < matrix.height(); i++) {
            for (int j = 0; j < matrix.width(); j++) {
                FloatList column = m.getColumn(j);

                for (int k = 0; k < height(); k++) {
                    matrix.values[i + j * matrix.height()] += values[i + k * height()] * column.getFloat(k);
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
    public float @NotNull [] multiply(final float @NotNull ... vector) {
        if (vector.length == 0) {
            return vector;
        }

        final float[] subject = Arrays.copyOf(vector, width());
        final int effectiveHeight = Math.min(height(), width());


        if (vector.length < width()) {
            ArrayUtil.fill(subject, vector.length, width(), 1);
        }

        for (int i = 0; i < width(); i++) {
            float value = 0;
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
                final float finalValue = value;
                ArrayUtil.setRange(vector, (k) -> vector[k] / finalValue, 0, vector.length);
            }
        }

        return vector;
    }

    static float[] copyToFloatArray(final @NotNull Object src) {
        if (src instanceof float[] a) {
            return a.clone();
        }

        if (src instanceof boolean[] a) {
            float[] ret = new float[a.length];

            ArrayUtil.setAll(ret, (i) -> a[i] ? 1 : 0);

            return ret;
        }

        if (src instanceof byte[] a) {
            return ListUtil.mapToFloat(ArrayUtil.asList(a), (byte e) -> e).toFloatArray();
        }

        if (src instanceof char[] a) {
            return ListUtil.mapToFloat(ArrayUtil.asList(a), (char e) -> e).toFloatArray();
        }

        if (src instanceof short[] a) {
            return ListUtil.mapToFloat(ArrayUtil.asList(a), (short e) -> e).toFloatArray();
        }

        if (src instanceof int[] a) {
            return ListUtil.mapToFloat(ArrayUtil.asList(a), (int e) -> e).toFloatArray();
        }

        if (src instanceof long[] a) {
            return ListUtil.mapToFloat(ArrayUtil.asList(a), (long e) -> e).toFloatArray();
        }

        if (src instanceof double[] a) {
            return ListUtil.mapToFloat(ArrayUtil.asList(a), (Double2FloatFunction) e -> (float) e).toFloatArray();
        }

        if (src instanceof Number[] a) {
            return ListUtil.transformToFloat(ArrayUtil.asList(a), Number::floatValue).toFloatArray();
        }

        throw new IllegalStateException("invalid matrix'es array: " + src);
    }

    static final class Row extends AbstractFloatList implements ImprovedFloatCollection, RandomAccess {
        private final float[] values;
        private final int width;
        private final int height;
        private final int offset;

        Row(float[] values, int width, int height, int offset) {
            this.values = values;
            this.width = width;
            this.height = height;
            this.offset = offset;
        }

        @Override
        public float set(int index, float k) {
            final int aIndex = height * index + offset;
            final float ret = values[aIndex];

            values[aIndex] = k;

            return ret;
        }

        @Override
        public float getFloat(int index) {
            final int aIndex = height * index + offset;

            return values[aIndex];
        }

        @Override
        public int size() {
            return width;
        }
    }
}
