package com.github.svegon.utils.math.matrix;

import com.github.svegon.utils.collections.ArrayUtil;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatList;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.RandomAccess;
import java.util.StringJoiner;

public final class Matrix4x3f implements Matrix<float[], FloatList>, Cloneable, RandomAccess {
    private final float[] values;

    public Matrix4x3f(final float @NotNull ... values) {
        this.values = values.length == 12 ? values : Arrays.copyOf(values, 12);
    }

    public Matrix4x3f() {
        this.values = new float[12];
    }

    public Matrix4x3f(final @NotNull AffineTransform transform) {
        this((float) transform.getScaleX(), (float) transform.getShearY(), 0,
                (float) transform.getShearX(), (float) transform.getScaleY(), 0,
                (float) transform.getTranslateX(), (float) transform.getTranslateY(), 0,
                0, 0, 0);
    }

    public Matrix4x3f(final @NotNull Matrix<?, ?> original) {
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
        return 3937 + Arrays.hashCode(values);
    }

    @Override
    public Matrix4x3f clone() {
        return new Matrix4x3f(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64).append("[");
        StringJoiner joiner = new StringJoiner(", ", " ", "");

        for (float f : getRow(0)) {
            joiner.add(String.valueOf(f));
        }

        builder.append(joiner);

        for (int i = 1; i < 3; i++) {
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
        return ArrayUtil.asList(values).subList(3 * index, 3 * (index + 1));
    }

    @Override
    public FloatList getRow(int index) {
        return new Matrixf.Row(values, 4, 3, Preconditions.checkElementIndex(index, width()));
    }

    @Override
    public void set(float @NotNull ... values) {
        System.arraycopy(values, 0, this.values, 0, Math.min(12, values.length));
    }

    @Override
    public @NotNull Matrixf transposed() {
        return new Matrixf(3, 4, values[0], values[3], values[6], values[9], values[1], values[4],
                values[7], values[10], values[2], values[5], values[8], values[11]);
    }

    @Override
    public void mul(@NotNull Matrix<float[], FloatList> m) {
        Preconditions.checkArgument(m.width() >= 4);
        Preconditions.checkArgument(m.height() >= 3);

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
            matrix = new Matrix4x3f();
        } else {
            matrix = new Matrixf(m.width(), 3);
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
        return 3;
    }

    @Override
    public float[] multiply(final float @NotNull ... vector) {
        if (vector.length == 0) {
            return vector;
        }

        float x = vector[0];
        float y = 1;
        float z = 1;
        float w = 1;

        if (vector.length == 2) {
            y = vector[1];
        } else if (vector.length == 3) {
            y = vector[1];
            z = vector[2];
        } else if (vector.length >= 4) {
            y = vector[1];
            z = vector[2];
            w = vector[3];
        }

        switch (vector.length) {
            case 1 -> {
                vector[0] = x * values[0] + values[3] + values[6] + values[9];
                vector[0] /= x * values[1] + values[4] + values[7] + values[10];
                vector[0] /= x * values[2] + values[5] + values[8] + values[11];
            }
            case 2 -> {
                vector[0] = x * values[0] + y * values[3] + values[6] + values[9];
                vector[1] = x * values[1] + y * values[4] + values[7] + values[10];
                z = x * values[2] + y * values[5] + values[8] + values[11];
                vector[0] /= z;
                vector[1] /= z;
            }
            default -> {
                vector[0] = x * values[0] + y * values[3] + z * values[6] + w * values[9];
                vector[1] = x * values[1] + y * values[4] + z * values[7] + w * values[10];
                vector[2] = x * values[2] + y * values[5] + z * values[8] + w * values[11];
            }
        }

        return vector;
    }

    private static float[] copyToFloatArray(Matrix<?, ?> m) {
        Preconditions.checkArgument(m.width() == 4);
        Preconditions.checkArgument(m.height() == 3);

        return Matrixf.copyToFloatArray(m.asArray());
    }
}
