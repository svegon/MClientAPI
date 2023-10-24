package com.github.svegon.utils.math.geometry.vector;

import org.jetbrains.annotations.Nullable;

public final class Vec4i extends Vec4t<int[], Vec4i> {
    public static final Vec4i ZERO = new Vec4i(0, 0, 0, 0);
    public static final Vec4i MIN = new Vec4i(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE,
            Integer.MIN_VALUE);
    public static final Vec4i MAX = new Vec4i(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
            Integer.MAX_VALUE);

    private final int x;
    private final int y;
    private final int z;
    private final int w;

    public Vec4i(int x, int y, int z, int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getW() {
        return w;
    }

    public Vec4i add(int x, int y, int z, int w) {
        return new Vec4i(getX() + x, getY() + y, getZ() + z, getW() + w);
    }

    public Vec4i multiplyEach(double x, double y, double z, double w) {
        return new Vec4i((int) (getX() * x), (int) (getY() * y), (int) (getZ() * z), (int) (getW() * w));
    }

    @Override
    public int[] toPrimitiveArray() {
        return new int[]{getX(), getY(), getZ(), getW()};
    }

    @Override
    public int[] toPrimitiveArray(int @Nullable [] ints) {
        if (ints == null || ints.length < 4) {
            return toPrimitiveArray();
        }

        ints[0] = getX();
        ints[1] = getY();
        ints[2] = getZ();
        ints[3] = getW();

        return ints;
    }

    @Override
    public Vec4i add(Vec4i other) {
        return add(other.getX(), other.getY(), other.getZ(), other.getW());
    }

    @Override
    public Vec4i substract(Vec4i other) {
        return add(-other.getX(), -other.getY(), -other.getZ(), -other.getW());
    }

    @Override
    public Vec4i multiply(double multiplier) {
        return multiplyEach(multiplier, multiplier, multiplier, multiplier);
    }

    @Override
    public Vec4i multiplyEach(Vec4i other) {
        return multiplyEach(other.getX(), other.getY(), other.getZ(), other.getW());
    }

    @Override
    public Vec4i divide(double divider) {
        return new Vec4i((int) (getX() / divider), (int) (getY() / divider), (int) (getZ() / divider),
                (int) (getW() * divider));
    }

    @Override
    public Vec4i neg() {
        return new Vec4i(-getX(), -getY(), -getZ(), -getW());
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
