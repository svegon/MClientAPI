package com.github.svegon.utils.math.matrix;

import com.github.svegon.utils.collections.ArrayUtil;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatList;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.StringJoiner;

public final class Matrix3x2f implements Matrix<float[], FloatList> {
    private float a00, a01, a10, a11, a02, a12;

    public Matrix3x2f() {
        a00 = a11 = 1; // loads identity
    }

    public Matrix3x2f(float @NotNull ... values) {
        set(values);
    }

    public Matrix3x2f(final @NotNull AffineTransform transform) {
        a00 = (float) transform.getScaleX();
        a10 = (float) transform.getShearY();
        a01 = (float) transform.getShearX();
        a11 = (float) transform.getScaleY();
        a02 = (float) transform.getTranslateX();
        a12 = (float) transform.getTranslateY();
    }

    public Matrix3x2f(final @NotNull Matrix<?, ?> original) {
        this(original instanceof Matrixf ? ((Matrixf) original).asArray() : Matrixf.copyToFloatArray(original));
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
        return 2945 + Arrays.hashCode(asArray());
    }

    @Override
    public Matrix3x2f clone() {
        Matrix3x2f clone;
        try {
            clone = (Matrix3x2f) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }

        return clone;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64).append("[");
        StringJoiner joiner = new StringJoiner(", ", " ", "");

        for (float f : getRow(0)) {
            joiner.add(String.valueOf(f));
        }

        builder.append(joiner);

        for (int i = 1; i < 2; i++) {
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
    public int width() {
        return 3;
    }

    @Override
    public int height() {
        return 2;
    }

    @Override
    public float @NotNull [] asArray() {
        return new float[]{a00, a10, a01, a11, a02, a12};
    }

    @Override
    public FloatList getColumn(int index) {
        return switch (index) {
            case 0 -> FloatList.of(a00, a10);
            case 1 -> FloatList.of(a01, a11);
            case 2 -> FloatList.of(a02, a12);
            default -> throw new IndexOutOfBoundsException("index " + index + " out of bounds for size 3");
        };
    }

    @Override
    public FloatList getRow(int index) {
        return switch (index) {
            case 0 -> FloatList.of(a00, a01, a02);
            case 1 -> FloatList.of(a10, a11, a12);
            default -> throw new IndexOutOfBoundsException("index " + index + " out of bounds for size 2");
        };
    }

    @Override
    public void set(float @NotNull [] values) {
        if (values.length >= 1) {
            a00 = values[0];

            if (values.length >= 2) {
                a01 = values[1];

                if (values.length >= 3) {
                    a10 = values[2];

                    if (values.length >= 4) {
                        a11 = values[3];

                        if (values.length >= 5) {
                            a02 = values[4];

                            if (values.length >= 6) {
                                a12 = values[5];
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public @NotNull Matrix<float[], FloatList> transposed() {
        return new Matrixf(2, 3, a00, a10, a02, a01, a11, a12);
    }

    @Override
    public void mul(@NotNull Matrix<float[], FloatList> m) {
        Preconditions.checkArgument(m.width() >= 3 && m.height() >= 2);

        a00 *= m.getColumn(0).getFloat(0);
        a01 *= m.getColumn(1).getFloat(0);
        a10 *= m.getColumn(0).getFloat(1);
        a11 *= m.getColumn(1).getFloat(1);
        a02 *= m.getColumn(2).getFloat(0);
        a12 *= m.getColumn(2).getFloat(1);
    }

    @Override
    public @NotNull Matrix<float[], FloatList> multiply(@NotNull Matrix<float[], FloatList> m) {
        Preconditions.checkArgument(3 == m.height());

        Matrixf matrix = new Matrixf(m.width(), 2);

        for (int i = 0; i < matrix.height(); i++) {
            FloatList row = getRow(i);

            for (int j = 0; j < matrix.width(); j++) {
                FloatList column = m.getColumn(j);

                for (int k = 0; k < 2; k++) {
                    matrix.asArray()[i + j * matrix.height()] += row.getFloat(k) * column.getFloat(k);
                }
            }
        }

        return matrix;
    }

    @Override
    public float[] multiply(float @NotNull [] vector) {
        switch (vector.length) {
            case 0 -> {}
            case 1 -> {
                float x = vector[0];
                vector[0] = x * a00 + a10 + a02;
                vector[0] /= x * a01 + a11 + a12;
            }
            case 2 -> {
                float x = vector[0];
                float y = vector[1];
                vector[0] = x * a00 + y * a10 + a02;
                vector[1] = x * a01 + y * a11 + a12;
            }
            default -> {
                float x = vector[0];
                float y = vector[1];
                float z = vector[2];
                vector[0] = x * a00 + y * a10 + z * a02;
                vector[1] = x * a01 + y * a11 + z * a12;
            }
        }

        return vector;
    }

    public void loadIdentity() {
        a01 = a10 = a02 = a12 = 0;
        a00 = a11 = 1;
    }
}
