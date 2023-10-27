package io.github.svegon.utils.math.matrix;

import io.github.svegon.utils.collections.ArrayUtil;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatList;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.RandomAccess;
import java.util.StringJoiner;

public final class Matrix4x4f implements Matrix<float[], FloatList>, Cloneable, RandomAccess {
    private final float[] values;

    public Matrix4x4f(final float @NotNull ... values) {
        this.values = values.length == 16 ? values : Arrays.copyOf(values, 16);
    }

    public Matrix4x4f() {
        this.values = new float[16];
    }

    public Matrix4x4f(final @NotNull AffineTransform transform) {
        this((float) transform.getScaleX(), (float) transform.getShearY(), 0, 0,
                (float) transform.getShearX(), (float) transform.getScaleY(), 0, 0,
                (float) transform.getTranslateX(), (float) transform.getTranslateY(), 0, 0,
                0, 0, 0, 0);
    }

    public Matrix4x4f(final @NotNull Matrix<?, ?> original) {
        this(copyToFloatArray(original));
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
        return 3968 + Arrays.hashCode(values);
    }

    @Override
    public Matrix4x4f clone() {
        return new Matrix4x4f(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(128).append("[");
        StringJoiner joiner = new StringJoiner(", ", " ", "");

        for (float f : getRow(0)) {
            joiner.add(String.valueOf(f));
        }

        builder.append(joiner);

        for (int i = 1; i < 4; i++) {
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
        return ArrayUtil.asList(values).subList(4 * index, 4 * (index + 1));
    }

    @Override
    public FloatList getRow(int index) {
        return new Matrixf.Row(values, 4, 4, Preconditions.checkElementIndex(index, width()));
    }

    @Override
    public void set(float @NotNull ... values) {
        System.arraycopy(values, 0, this.values, 0, Math.min(16, values.length));
    }

    @Override
    public void transpose() {
        int i = 0;

        while (i < 4){
            int j = i + 1;

            while (j < 4) {
                int index = 4 * j + i;
                int index2 = j++ + 4 * i;

                float f = values[index2];
                values[index2] = values[index];
                values[index] = f;
            }

            i++;
        }
    }

    @Override
    public @NotNull Matrix<float[], FloatList> transposed() {
        Matrix4x4f copy = clone();
        copy.transpose();
        return copy;
    }

    @Override
    public void mul(@NotNull Matrix<float[], FloatList> m) {
        Preconditions.checkArgument(width() == m.width());
        Preconditions.checkArgument(height() == m.height());

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                values[4 * i + j] *= m.getColumn(i).getFloat(j);
            }
        }
    }

    @Override
    public @NotNull Matrix<float[], FloatList> multiply(@NotNull Matrix<float[], FloatList> m) {
        Preconditions.checkArgument(4 == m.height());

        Matrix<float[], FloatList> matrix;

        if (m.width() == 4) {
            matrix = new Matrix4x4f();
        } else {
            matrix = new Matrixf(m.width(), 4);
        }

        for (int i = 0; i < matrix.height(); i++) {
            for (int j = 0; j < matrix.width(); j++) {
                FloatList column = m.getColumn(j);

                for (int k = 0; k < height(); k++) {
                    matrix.asArray()[i + j * matrix.height()] += values[i + k * height()] * column.getFloat(k);
                }
            }
        }

        return matrix;
    }

    @Override
    public int width() {
        return 4;
    }

    @Override
    public int height() {
        return 4;
    }

    @Override
    public float[] multiply(final float @NotNull ... vector) {
        if (vector.length == 0) {
            return vector;
        }

        float[] subject = new float[]{vector[0], 1, 1, 1};

        if (vector.length == 2) {
            subject[1] = vector[1];
        } else if (vector.length == 3) {
            subject[1] = vector[1];
            subject[2] = vector[2];
        } else if (vector.length >= 4) {
            subject[1] = vector[1];
            subject[2] = vector[2];
            subject[3] = vector[3];
        }

        vector[0] = subject[0] * values[0] + subject[1] * values[4] + subject[2] * values[8] + subject[3] * values[12];

        for (int i = 1; i < 4; i++) {
            final float value = subject[0] * values[i] + subject[1] * values[i + 4] + subject[2] * values[i + 8]
                    + subject[3] * values[i + 12];

            if (i < vector.length) {
                vector[i] = value;
            } else {
                ArrayUtil.setRange(vector, (j) -> vector[j] / value, 0, vector.length);
            }
        }

        return vector;
    }

    private static float[] copyToFloatArray(Matrix<?, ?> m) {
        Preconditions.checkArgument(m.width() == 4);
        Preconditions.checkArgument(m.height() == 4);

        return Matrixf.copyToFloatArray(m.asArray());
    }
}
