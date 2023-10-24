package com.github.svegon.utils.math.geometry.vector;

import org.jetbrains.annotations.Nullable;

public final class Vec4f extends Vec4t<float[], Vec4f> {
    public static final Vec4f ZERO = new Vec4f(0, 0, 0, 0);
    public static final Vec4f MIN = new Vec4f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY,
            Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
    public static final Vec4f MAX = new Vec4f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY,
            Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    public static final Vec4f NAN = new Vec4f(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

    private final float x;
    private final float y;
    private final float z;
    private final float w;

    public Vec4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getW() {
        return w;
    }

    public Vec4f add(float x, float y, float z, float w) {
        return new Vec4f(getX() + x, getY() + y, getZ() + z, getW() + w);
    }

    public Vec4f multiplyEach(float x, float y, float z, float w) {
        return new Vec4f(getX() * x, getY() * y, getZ() * z, getW() * w);
    }

    public Vec4f divideEach(float x, float y, float z, float w) {
        return new Vec4f(getX() * x, getY() * y, getZ() * z, getW() * w);
    }

    @Override
    public float[] toPrimitiveArray() {
        return new float[]{getX(), getY(), getZ(), getW()};
    }

    @Override
    public float[] toPrimitiveArray(float @Nullable [] floats) {
        if (floats == null || floats.length < 4) {
            return toPrimitiveArray();
        }

        floats[0] = getX();
        floats[1] = getY();
        floats[2] = getZ();
        floats[3] = getW();

        return floats;
    }

    @Override
    public Vec4f add(Vec4f other) {
        return add(other.getX(), other.getY(), other.getZ(), other.getW());
    }

    @Override
    public Vec4f substract(Vec4f other) {
        return add(-other.getX(), -other.getY(), -other.getZ(), -other.getW());
    }

    @Override
    public Vec4f multiply(double multiplier) {
        float f = (float) multiplier;
        return multiplyEach(f, f, f, f);
    }

    @Override
    public Vec4f multiplyEach(Vec4f other) {
        return multiplyEach(other.getX(), other.getY(), other.getZ(), other.getW());
    }

    @Override
    public Vec4f divide(double divider) {
        float f = (float) divider;
        return divideEach(f, f, f, f);
    }

    @Override
    public Vec4f neg() {
        return new Vec4f(-getX(), -getY(), -getZ(), -getW());
    }

    @Override
    public String toString() {
        return getX() + " " + getY() + " " + getZ() + " " + getW();
    }

    @Override
    public double getDouble(int index) {
        return switch (index) {
            case 0 -> getX();
            case 1 -> getY();
            case 2 -> getZ();
            case 3 -> getW();
            default -> throw new IndexOutOfBoundsException();
        };
    }
}
